package apiRecommendation.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import apiRecommendation.Action.APIRetrieval;
import apiRecommendation.Action.APIWarningMessage;
import apiRecommendation.Action.ASTTraversal;
import apiRecommendation.Action.MarkWarningHandler;
import apiRecommendation.Action.PrintResult;

/**
 * Listen to changes in the java element 
 * and build the AST
 */
public class JavaListener implements IElementChangedListener{

	@Override
	public void elementChanged(ElementChangedEvent event) {
		// TODO Auto-generated method stub
		if(event.getType() == ElementChangedEvent.POST_RECONCILE){
			
			IJavaElementDelta delta= event.getDelta();
			CompilationUnit cu = delta.getCompilationUnitAST();
			ASTTraversal visit = new ASTTraversal();
			cu.accept(visit);
			
			/**
			 * Retrieve the method invocation used in 
			 * the program
			 */
			List<String> keysbeforeOffset = visit.getkeysbeforeOffset();
			List<String> keysafterOffset = visit.getkeysafterOffset();
			
			List<String> result = new ArrayList<String>();
			APIRetrieval apiRecommend = new APIRetrieval();
			
			/**
			 * Retrieve the recommendation result
			 */
			if((keysbeforeOffset.size() + keysafterOffset.size()) >= 1){
				try {
					result = apiRecommend.getResult(keysbeforeOffset, keysafterOffset);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * Get the information of every API used 
			 */
			ArrayList<String> keysInfo = new ArrayList<String>();
			keysInfo = visit.getKeyInfo();
			
			/**
			 * Check if there is any warning message
			 */
			TreeMap<Integer,Integer> warningList = new TreeMap<Integer,Integer>();
			APIWarningMessage checkList = new APIWarningMessage(keysInfo);
			checkList.checkAPI();
			warningList = checkList.getWarningList();
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); 
			IJavaElement create = JavaCore.create(delta.getElement().getHandleIdentifier());
			IPath path = create.getPath();
			IFile file = root.getFile(path);
				
			ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(file);
			IDocument document;
			try {
				/**
				 * Every changes in java element will remove all the warning message
				 * and re-input the warning message if any
				 */
				document = new Document(compilationUnit.getSource());
				MarkWarningHandler markingErrorHandler = new MarkWarningHandler(file, document);
				markingErrorHandler.removeHandler();
				/**
				 * Display the warning message if any
				 */
				if(warningList.size() > 0){
					try {
						markingErrorHandler.addError(warningList);
					} catch (CoreException | BadLocationException e) {
						e.printStackTrace();
					}	
				}
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
			/**
			 * Print the result to the eclipse view
			 */
			if(result.size() >= 1){
				if(!result.get(0).equals("do nothing")){
					PrintResult print = new PrintResult();
					print.printResult(result);
				}
			}
		}
	}
	
}
