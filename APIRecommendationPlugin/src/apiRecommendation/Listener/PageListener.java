package apiRecommendation.Listener;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

/**
 * Listen to the active editor in the workbench 
 * and listen to changes of the caret position
 */
public class PageListener implements IPartListener2 {
	
	public static int offset;
	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		
		if(activeEditor != null && activeEditor.getClass().getName().endsWith("CompilationUnitEditor")){
					
			offset = 0;
			Control control = activeEditor.getAdapter(Control.class);
			
			if (control instanceof StyledText) {
				StyledText text = (StyledText)control;
				text.addCaretListener(new CaretListener() {
					@Override
					public void caretMoved(CaretEvent event) {
						IEditorInput input = activeEditor.getEditorInput();  
						ITextSelection textSelection = (ITextSelection) activeEditor.getSite().getSelectionProvider().getSelection();
						offset = textSelection.getOffset();
					}
				});
			}	  
			
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef){
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub\
	}
}
