package apiRecommendation.Action;

import java.util.List;
import org.eclipse.swt.widgets.Display;
import apiRecommendation.View.View;

public class PrintResult {
	
	/**
	 * Print the result into plugin view
	 */
	public void printResult(List<String> result){
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				View.viewer.setInput(result);
				View.viewer.refresh();
			}
		});
	}
	
}
