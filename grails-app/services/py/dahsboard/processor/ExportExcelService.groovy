package py.dahsboard.processor

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import pl.touk.excel.export.XlsxExporter
import pl.touk.excel.export.getters.LongToDatePropertyGetter
import pl.touk.excel.export.getters.MessageFromPropertyGetter
import org.springframework.context.i18n.LocaleContextHolder

class ExportExcelService {

	static transactional = false

	def messageSource

	def grailsApplication

    def String createExcel(data) {

    	log.info "ExportExcelService createExcel()"

    	def date = new Date()

    	def path = grailsApplication.config.file.tmp.path
    	def xlsxName = grailsApplication.config.file.xlsx.name + 
    					date.format(grailsApplication.config.file.date.format) + 
    						grailsApplication.config.file.xlsx.ext
    	def xlsxPath = path + xlsxName

    	def withProperties = ['cust_id', 'new_balance', 'old_balance', 'difference']

    	def headers = [message('header.customer.id'), message('header.new.balance'),
    						 message('header.old.balance'), message('header.difference')]

    	try{
			new XlsxExporter(xlsxPath).
				fillHeader(headers).
	    			add(data, withProperties).
	    				save()
	    } catch (Exception e){
 			log.info "Error creating ${xlsxName} file."
		}

    	log.info "${xlsxName} was created successfully."

    	return xlsxName
    }

    //TODO sacar a Utils
    def message(key){
    	messageSource.getMessage('py.dashboard.processor.excel.' + key, null, 'No message for ' + 
    		key + ' key', LocaleContextHolder.locale)
    }
}