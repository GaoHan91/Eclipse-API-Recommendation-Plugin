package apiRecommendation.Action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import apiRecommendation.Listener.PageListener;

/**
 * The AST Traversal of the current active java editor.
 * Get the invocation list and its offset position
 */
public class ASTTraversal extends ASTVisitor{
	
	List<String> invocationListBeforeOffset = new ArrayList<String>();
	List<String> invocationListAfterOffset = new ArrayList<String>();
	ArrayList<String> keysInfo = new ArrayList<String>();
	
	public void postVisit(ASTNode node){
		
		if(node instanceof Expression){
			
			Expression exp = (Expression) node;
		  
			if (exp instanceof MethodInvocation) {
			  
				MethodInvocation mi = (MethodInvocation) exp;
				IMethodBinding method = mi.resolveMethodBinding();
				String qualified_name = method.getDeclaringClass().getQualifiedName();
				String method_name = method.getName();
									
				StringBuilder name = new StringBuilder();
				name.append(qualified_name + "." + method_name);
			
				int nodeOffset = mi.getStartPosition();
				
				StringBuilder keyInfo = new StringBuilder();
				keyInfo.append(nodeOffset + "," + mi.getLength() + "," + name.toString());
				keysInfo.add(keyInfo.toString());
				
				IMethod resolvedMethod = (IMethod)method.getJavaElement();
				/**This method determined if a particular method invocation
				 * is a class method.
				 * true: Java API or third party API
				 * false: self-declared method
				 * */
				if(resolvedMethod.isBinary()){
					if(nodeOffset < PageListener.offset){
						invocationListBeforeOffset.add(name.toString());
					}else if(nodeOffset >= PageListener.offset){
						invocationListAfterOffset.add(name.toString());
					}
				}else{
					if(nodeOffset <= PageListener.offset){
						invocationListBeforeOffset.add("NA");
					}else if(nodeOffset > PageListener.offset){
						invocationListAfterOffset.add("NA");
					}
				}
				
			}	
		}
	}
	
	
	public List<String> getkeysbeforeOffset(){
		return invocationListBeforeOffset;
	}
	
	public List<String> getkeysafterOffset(){
		return invocationListAfterOffset;
	}
	
	public List<String> getallkeys(){
		List<String> allkeys = new ArrayList<String>();
		allkeys.addAll(invocationListBeforeOffset);
		allkeys.addAll(invocationListAfterOffset);
		return allkeys;
	}
	
	public ArrayList<String> getKeyInfo(){
		return keysInfo;
	}
	
}
