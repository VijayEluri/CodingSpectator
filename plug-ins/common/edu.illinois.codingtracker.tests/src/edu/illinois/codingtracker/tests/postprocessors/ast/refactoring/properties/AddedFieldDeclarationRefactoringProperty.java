/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.codingtracker.tests.postprocessors.ast.refactoring.properties;



/**
 * This class represents an added declaration of a field.
 * 
 * @author Stas Negara
 * 
 */
public class AddedFieldDeclarationRefactoringProperty extends RefactoringProperty {


	public AddedFieldDeclarationRefactoringProperty(String entityName, long entityNameNodeID, long activationTimestamp) {
		super(activationTimestamp);
		addAttribute(RefactoringPropertyAttributes.ENTITY_NAME, entityName);
		addAttribute(RefactoringPropertyAttributes.ENTITY_NAME_NODE_ID, entityNameNodeID);
	}

}
