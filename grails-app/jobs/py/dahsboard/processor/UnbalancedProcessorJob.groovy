package py.dahsboard.processor

import org.springframework.context.i18n.LocaleContextHolder

class UnbalancedProcessorJob {

    def unbalancedProcessorService

    static triggers = {
      // Repeat every day at time. Ej. 0s 30m 05h
      cron name: 'UNBALANCED_PROCESSOR_PRD', startDelay: 10000, cronExpression: '0 41 17 1/1 * ? *'			
    }

    def execute() {

        log.info "Unbalance Processor Job is executing..."

        unbalancedProcessorService.doProcessing()
    }
}