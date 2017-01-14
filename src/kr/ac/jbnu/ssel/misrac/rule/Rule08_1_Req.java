package kr.ac.jbnu.ssel.misrac.rule;

import java.util.HashSet;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDoStatement;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;

import kr.ac.jbnu.ssel.misrac.rulesupport.AbstractMisraCRule;
import kr.ac.jbnu.ssel.misrac.rulesupport.MessageFactory;
import kr.ac.jbnu.ssel.misrac.rulesupport.ViolationMessage;

/**
 * MISRA-C:2004  Rule  8.1:  (Required)	
 * Functions shall have prototype declarations and the prototype shall 
 * be visible at both the function definition and call.
 * 
 * The use of prototypes enables the compiler to check the integrity of function definitions and calls. 
 * Without prototypes the compiler is not obliged to pick up certain errors in function calls 
 * (e.g. different number of arguments from the function body, mismatch in types of arguments 
 * between call and definition). Function interfaces have been shown to be a cause of considerable problems, 
 * and therefore this rule is considered very important.
 * 
 * The recommended method of implementing function prototypes for external functions is to declare the function 
 * (i.e. give the function prototype) in a header file, and then include the header file in all those code 
 * files that need the prototype (see Rule ).
 * 
 * The provision of a prototype for a function with internal linkage is a good programming practice.
 * 
 * TODO: functionDeclarations�� '#include'�ȿ� �ִ� �Լ� ���ǵ� �����ͼ� �� ������ ��ƾ���.  
 * 
 * @author stkim
 *
 */
public class Rule08_1_Req extends AbstractMisraCRule {
	private static HashSet<IASTSimpleDeclaration> prototypes = new HashSet<IASTSimpleDeclaration>();
	
	public Rule08_1_Req(IASTTranslationUnit ast) {
		super("Rule08_1_Req", false, ast);
		shouldVisitPreprocessor = true;
		shouldVisitExpressions = true;
		shouldVisitStatements = true;
	}

	@Override
	protected int visit(IASTSimpleDeclaration simpleDeclaration) {
		
		IASTDeclSpecifier specifier = simpleDeclaration.getDeclSpecifier();
		IASTNode[] children = specifier.getChildren();
		
		boolean isFunctionDeclaration = false;
		for (IASTNode iastNode : children) {
			if( iastNode instanceof IASTFunctionDeclarator)
			{
				isFunctionDeclaration = true;
			}
		}
		
		if(isFunctionDeclaration)
		{
			prototypes.add(simpleDeclaration);
		}

		return super.visit(simpleDeclaration);
	}
	
	/**
	 * Rule: function����ΰ� prototype���� ����Ǿ��־����. ��, declspecifier�� declarator�� ������ �����ؾ���. 
	 * 			declspecifier�� ���� �����ؾ��ϰ�, declarator�κ��� �Ķ������ �̸��� �����ϰ� �ٸ����� �����ؾ���.  
	 * 
	 */
	@Override
	protected int visit(IASTFunctionDefinition functionDefinition) {
		
		IASTDeclSpecifier specifier = functionDefinition.getDeclSpecifier();
		IASTFunctionDeclarator funcDeclarator = functionDefinition.getDeclarator();
		
		if( checkConformanceOfPrototype(functionDefinition))
		{
			String msg = MessageFactory.getInstance().getMessage(002);
			
		}
		
		
		return super.visit(functionDefinition);
	}
	
	/**
	 * function definition�� prototype�� �������� �Ǵ�
	 * 
	 * @param functionDefinition
	 * @return
	 */
	private boolean checkConformanceOfPrototype(IASTFunctionDefinition functionDefinition) {

		return true;
	}

	/**
	 * Rule: functionȣ��κ��� prototype���� ����Ǿ� �־����.
	 */
	@Override
	protected int visit(IASTFunctionCallExpression expression) {
		// TODO Auto-generated method stub
		return super.visit(expression);
	}

}
