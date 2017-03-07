package apiRecommendation.Action;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class MarkWarningHandler {
	
	private IFile file;
	private IDocument document;
	
	
	public MarkWarningHandler(IFile file, IDocument document){
		super();
		this.file = file;
		this.document = document;
	}
	
	/**
	 * Remove all the warning message
	 */
	public void removeHandler() throws CoreException{
		 file.deleteMarkers(IMarker.PROBLEM, true, file.DEPTH_INFINITE);     
	}
	
	/**
	 * Method to input the warning message
	 * to the java file
	 */
	public void addError(TreeMap<Integer,Integer> warningLocation) throws CoreException, BadLocationException{
		
		for(Map.Entry<Integer, Integer> location: warningLocation.entrySet()){
			
			Integer offsetValue = location.getKey();
			Integer length = location.getValue();
			
			int lineNumber = document.getLineOfOffset(offsetValue);
			 
			IMarker m1 = file.createMarker(IMarker.PROBLEM);
			m1.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			m1.setAttribute(IMarker.MESSAGE, " Warning : check API Used ");
			m1.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			m1.setAttribute(IMarker.CHAR_START, offsetValue);
			m1.setAttribute(IMarker.CHAR_END, offsetValue + length);
			
		}
	}
	
}
