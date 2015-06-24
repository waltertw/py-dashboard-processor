package py.dahsboard.processor

import grails.transaction.Transactional
import grails.plugin.mail.*
import org.springframework.context.i18n.LocaleContextHolder


class SendEmailService {

	static transactional = false

	def messageSource

	def grailsApplication
	def mailService = new MailService()

    def sendEmailWithXlsxAttachment(xlsxName) {

    	log.info "SendEmailService sendEmailWithXlsxAttachment()"

    	def path = grailsApplication.config.file.tmp.path
    	def xlsxPath = path + xlsxName
    	def xlsxMime = grailsApplication.config.file.xlsx.mime

    	def mailSubject = message('subject')
    	def mailBody = message('body') + new Date()

    	//TODO ver si es necesario setear dinamicamente
    	def mailList = grailsApplication.config.mail.list
    	List<String> recipients = mailList.split(',').collect { it.trim() }

    	try{
	        mailService.sendMail {
	        	multipart true
			  	to recipients 
			  	subject mailSubject
			  	body mailBody
	          	attachBytes xlsxName, xlsxMime, new File(xlsxPath).readBytes()
	   		}
   		} catch (Exception e){
 			log.info "Error sending email"
		}

   		log.info "Email with ${xlsxName} attachment to ${mailList} was sent successfully."
    }

    //TODO sacar a Utils
    def message(key){
    	messageSource.getMessage('py.dashboard.processor.mail.' + key, null, 'No message for ' + 
    		key + ' key', LocaleContextHolder.locale)
    }
}