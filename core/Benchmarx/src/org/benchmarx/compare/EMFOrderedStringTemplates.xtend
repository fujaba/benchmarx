package org.benchmarx.compare
 
import java.util.List


public class EMFOrderedStringTemplates {
	
	def objToString(String className, List<String> mappings, List<String> attributes) {
		if(mappings.size == 0 && attributes.size == 0)
			return '''
				«className» { empty|undefined }
			'''
		else
			return '''
				«className» {
					«FOR s : attributes»
					«s» 
					«ENDFOR»
					«FOR s : mappings»
					«s» 
					«ENDFOR»
				}
			'''
	}
	
	def attributeToString(String attribute, String value) {
		return '''
			«attribute» = «value»
		'''
	}
	
	def singleMappingToString(String fieldName, String entry) {
		return '''
			«fieldName» -> 
				«entry»
		'''
	}
	
	def manyMappingToString(String listName, List<String> entries) {
		return '''
			«listName» = [
				«FOR e : entries SEPARATOR ","»
				«e»
				«ENDFOR»
			]
		'''
	}
}
