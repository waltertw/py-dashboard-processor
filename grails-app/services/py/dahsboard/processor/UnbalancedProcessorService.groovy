package py.dahsboard.processor

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class UnbalancedProcessorService {

	def sessionFactory
	def sendEmailService
	def exportExcelService

	Long time = null

	// Query desbalanceados
	// TODO Remover limite de resultados para pruebas (ROWNUM <= 2) 
	def queryStr = 
	"select  cust_id, nvl(sum_bill_unpaid,0) + nvl(sum_available_credit,0) saldo_nuevo, saldo_viejo, " + 
		"saldo_viejo - (nvl(sum_bill_unpaid,0) + nvl(sum_available_credit,0)) dif " + 
	"from ( " + 
		"select b.cust_id, SUM (NVL (b.amount - NVL (b.pay_amount, 0), 0)) sum_bill_unpaid, " + 
			"NVL (SUM (ac.available_amount) * -1, 0) sum_available_credit, " + 
				"NVL (SUM (NVL (cp.invoice_credit, 0)), 0) saldo_viejo " + 
		"from orange.bill b FULL OUTER JOIN orange.available_credit ac on (b.cust_id=ac.cust_id) " + 
			"FULL OUTER JOIN orange.credit_policy_data cp on (b.cust_id=cp.cust_id) " + 
		"where bill_status = 'A' " + 
		"and pay_status = 'N' " + 
		"and opened = 'N' " + 
		"and billed = 'Y' " + 
		"group by b.cust_id) aux " + 
	"where (ROWNUM <= 2) and ((NVL (sum_bill_unpaid, 0) + NVL (sum_available_credit, 0)) != saldo_viejo)"

	// Prueba - Query desbalanceados extendida
	def queryStr2 = 
	"select  cust_id, nvl(sum_bill_unpaid,0) + nvl(sum_available_credit,0) saldo_nuevo, saldo_viejo, " + 
		"saldo_viejo - (nvl(sum_bill_unpaid,0) + nvl(sum_available_credit,0)) dif " + 
	"from ( " + 
		"select b.cust_id, SUM (NVL (b.amount - NVL (b.pay_amount, 0), 0)) sum_bill_unpaid, " + 
			"NVL (SUM (ac.available_amount) * -1, 0) sum_available_credit, " + 
				"NVL (SUM (NVL (cp.invoice_credit, 0)), 0) saldo_viejo " + 
		"from orange.bill b FULL OUTER JOIN orange.available_credit ac on (b.cust_id=ac.cust_id) " + 
			"FULL OUTER JOIN orange.credit_policy_data cp on (b.cust_id=cp.cust_id) " + 
		"where bill_status = 'A' " + 
		"and pay_status = 'N' " + 
		"and opened = 'N' " + 
		"and billed = 'Y' " + 
		"group by b.cust_id) aux " + 
	"where (ROWNUM <= 2) and " + 
		"NOT EXISTS (" +
    		"SELECT /*+ INDEX(DEL IDX_CUST_PROC)*/ " +
           		"1 " +
      		"FROM orange.document_event_log del " +
     		"WHERE del.cust_id = aux.cust_id " +
       		"AND del.processed = 'N' " +
       		"AND ROWNUM < 2) " +
	 "and ((NVL (sum_bill_unpaid, 0) + NVL (sum_available_credit, 0)) != saldo_viejo)"

    def doProcessing() {

    	log.info "UnbalancedProcessorService doProcessing()"

    	def unbalancedCustomers = getUnbalancedCustomers()

    	def attachmentName = exportExcelService.createExcel(unbalancedCustomers)

    	sendEmailService.sendEmailWithXlsxAttachment(attachmentName)
    }

    def getUnbalancedCustomers(){

    	log.info "UnbalancedProcessorService getUnbalancedCustomers()"

    	log.info "Query will be run ${queryStr}"

    	print queryStr

    	def results = []
    	def count = 0
    	def session = sessionFactory.getCurrentSession()
		Sql sql = new Sql(sessionFactory.currentSession.connection())

		startTime()

		try{
			sql.eachRow queryStr, {row 	-> 	results << new UnbalancedCustomer(cust_id: row.cust_id, new_balance: row.saldo_nuevo, old_balance: row.saldo_viejo).save()
											count++
									}
		} catch (Exception e){
 			log.info "Error running query."
		}

		print "La query tardo ${getTotalTime()} milisegundos y retorno ${count} registros."
		//log.info "Query was succesful. It take ${getTotalTime()} miliseconds and it returned ${count} rows"
		return results
    }

    protected void startTime() {
		time = Calendar.getInstance().getTimeInMillis();
	}
	
	protected Long getTotalTime() {
		Long timeAux = 0
		if(time){
			timeAux = Calendar.getInstance().getTimeInMillis() - time;
		}
		time = null;
		return timeAux;
	}
}