package apiRecommendation.Action;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class JavaEditorAction {
	
	public void insertToJavaEditor(String addToEditor) throws BadLocationException, JavaModelException{
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor(); 
		IEditorInput input = editor.getEditorInput();  
		IDocument document=(((ITextEditor)editor).getDocumentProvider()).getDocument(input);
		
		if(editor instanceof ITextEditor && editor.getTitle().toString().endsWith("java")){
			
			String[] breakString = addToEditor.split("\\.");
			String methodCall = "." + breakString[breakString.length - 1];
			String importStatement = addToEditor.replace(methodCall, "");
			
			addMethodToEditor(editor, document, methodCall);
			addImportToEditor(document, importStatement);
			//checkImport();
		
		}
	
	}
	
	/**
	 * This method insert import statement into the java editor
	 * if the import statement could not be found in the 
	 * current java editor
	 * @param document - the source code document
	 * @param importStatement - import to be input
	 */
	private void addImportToEditor(IDocument document, String importStatement) {
		
		// TODO Auto-generated method stub
		String source = document.get();
	    CompilationUnit cu = parse(source);
	    List<ImportDeclaration> importUsed = cu.imports();
	    
	    boolean included = false;
	    for(ImportDeclaration importName: importUsed){
	    	if(importName.toString().contains(importStatement)){
	    		included = true;
	    		break;
	    	}
	    }
	    
	    if(!included){
	    	
	    	AST ast = cu.getAST();
	    	ImportDeclaration addImport = ast.newImportDeclaration();
	    	addImport.setName(ast.newName(importStatement));
	    	ASTRewrite rewriter = ASTRewrite.create(ast);
	    	ListRewrite addToAST = rewriter.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
	    	addToAST.insertLast(addImport, null);
	    	TextEdit edits = rewriter.rewriteAST(document, null);
	    	UndoEdit undo = null;
	    	try {
	    	    undo = edits.apply(document);
	    	} catch(MalformedTreeException e) {
	    	    e.printStackTrace();
	    	} catch(BadLocationException e) {
	    	    e.printStackTrace();
	    	}
	    	
	    }
	      
	}
	
	/**
	 * This method input the method name
	 * into the active java editor
	 */
	private void addMethodToEditor(IEditorPart editor, IDocument document, String methodCall) throws BadLocationException {
		
		// TODO Auto-generated method stub
		ITextSelection textSelection = (ITextSelection) editor.getSite().getSelectionProvider().getSelection();
		int currentOffset = textSelection.getOffset();
		int lineNumber = document.getLineOfOffset(currentOffset);
		
		int lineStartingOffset = document.getLineOffset(lineNumber);
		int lineLength = document.getLineLength(lineNumber);
			
		StringBuilder space = new StringBuilder();
			
		for(int i = lineStartingOffset; i < (lineStartingOffset + lineLength); i++){
			char content = document.getChar(i);
			if(content == ' ' || content == '\t'){
				space.append(content);
			}else{
				break;
			}
				
		}
			
		String textToInsert = space.toString() + methodCall;
		document.replace(currentOffset, 0, "\n" + textToInsert + ";");
		
	}
	
	 private static CompilationUnit parse(String source) {
         ASTParser parser = ASTParser.newParser(AST.JLS3);
         parser.setKind(ASTParser.K_COMPILATION_UNIT);
         parser.setSource(source.toCharArray());
         parser.setResolveBindings(true);
         parser.setBindingsRecovery(true);
         parser.setStatementsRecovery(true);
         parser.setIgnoreMethodBodies(true);
         return (CompilationUnit) parser.createAST(null); 
	 }

}
