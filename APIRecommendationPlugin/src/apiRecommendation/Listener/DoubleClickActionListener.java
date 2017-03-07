package apiRecommendation.Listener;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import apiRecommendation.Action.JavaEditorAction;
import apiRecommendation.View.View;

/**
 * Listen to double click action in the plugin view
 */
public class DoubleClickActionListener implements IDoubleClickListener {

	@Override
	public void doubleClick(DoubleClickEvent event) {
		// TODO Auto-generated method stub
		IStructuredSelection selection = (IStructuredSelection) View.viewer.getSelection();
		if(!selection.toString().contains("API") && !selection.toString().equals("")){
			String addToEditor= selection.toString().split(":")[0];
			addToEditor = addToEditor.trim().replace("[", "");
			/**
			 * JavaEditorAction - task is to input the select text 
			 * in the active java editor
			 */
			JavaEditorAction addStatement = new JavaEditorAction();
			try {
				try {
					addStatement.insertToJavaEditor(addToEditor);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

}
