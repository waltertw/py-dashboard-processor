package py.dahsboard.processor

class UnbalancedCustomer {

	Long cust_id

	Double new_balance

	Double old_balance

    static constraints = {
    }

	//TODO eliminar login de inserts
    static mapping = {
		datasource 'nw'
		version false
		table 'UNBALANCED_CUSTOMERS'
	}

	def Double getDifference(){
		old_balance - new_balance 
	}
}
