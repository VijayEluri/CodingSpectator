/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.codingtracker.tests.postprocessors.ast.refactoring.properties;



/**
 * This class represents changing a variable's name in its declaration.
 * 
 * @author Stas Negara
 * 
 */
public class ChangedVariableNameInDeclarationRefactoringProperty extends RefactoringProperty {


	private ChangedVariableNameInDeclarationRefactoringProperty() {

	}

	public ChangedVariableNameInDeclarationRefactoringProperty(String oldEntityName, String newEntityName) {
		addAttribute(RefactoringPropertyAttributes.OLD_ENTITY_NAME, oldEntityName);
		addAttribute(RefactoringPropertyAttributes.NEW_ENTITY_NAME, newEntityName);
	}

	@Override
	protected RefactoringProperty createFreshInstance() {
		return new ChangedVariableNameInDeclarationRefactoringProperty();
	}

}