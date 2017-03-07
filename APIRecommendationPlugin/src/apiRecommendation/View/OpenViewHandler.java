package apiRecommendation.View;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenViewHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		if(window == null)
			return null;
		IWorkbenchPage page = window.getActivePage();
		if(page == null)
			return null;
		try{
			page.showView("Learn_Plugin.view");
		}catch(PartInitException e){
			System.out.println("There is an error opening the view");
		}
		
		return null;
	}

}
