package apiRecommendation.View;

import java.awt.Label;
import java.io.IOException;
import java.util.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import apiRecommendation.Action.Data;
import apiRecommendation.Listener.DoubleClickActionListener;
import apiRecommendation.Listener.JavaListener;
import apiRecommendation.Listener.PageListener;


public class View extends ViewPart{
	
	public static final String ID = "Learn_Plugin.view";
	public static TableViewer viewer;
	
	public static final String firstTableID = "Latest API detected";
	public static final String secondTableID = "Recommendation based on latest API detected";
	
	private List<String> printResult = new ArrayList<String>();

	private class APILabelProvider extends ColumnLabelProvider {
		
		/**
		public Color getForeground(Object element){
			
			Device device = Display.getCurrent();
			Color blue = new Color (device, 0, 0, 255);
			Color white = new Color (device, 255, 255, 255);
			ArrayList list = (ArrayList) viewer.getInput();
		    int index = list.indexOf(element);
		    if (index == 0) {
		    	return white;
		    }else{
		    	return null;
		    }
		    
		 
		}
		*/
		
		public Color getBackground(Object element) {
			
			Device device = Display.getCurrent();
			Color darkerGray = new Color (device, 210, 210, 210);
			Color lighterGray = new Color (device, 240, 240, 240);
		    ArrayList list = (ArrayList) viewer.getInput();
		    int index = list.indexOf(element);
		    if (index == 0) {
		        return darkerGray;
		    }
		    if(list.get(index).equals("")){
		    	return null;
		    }
		    if(list.get(index).equals(secondTableID)){
		    	return darkerGray;
		    }
		    return lighterGray;
		}
		
		@Override
		public String getText(Object element) {
			return super.getText(element);
		}    
		
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Data Data = new Data();
		try {
			Data.loadBigram();
			Data.loadreversebigram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		viewer = new TableViewer(parent, SWT.SINGLE);
		Table table = viewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		int[] columnWidths = new int[] {1000};
		int[] columnAlignments = new int[]{SWT.LEFT};
		for(int i = 0; i < columnWidths.length; i++){
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
		
		viewer.setLabelProvider(new APILabelProvider());
		viewer.setContentProvider(new ArrayContentProvider());
		List<String> initialResult = new ArrayList<String>();
		initialResult.add("No method call is detected");
		viewer.setInput(createInitialDataModel(initialResult));
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new PageListener());
		JavaCore.addElementChangedListener(new JavaListener()); 
		viewer.addDoubleClickListener(new DoubleClickActionListener());
		
	}
    
	public void dispose() {
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(new PageListener());
		JavaCore.removeElementChangedListener(new JavaListener());
		viewer.removeDoubleClickListener(new DoubleClickActionListener());
		super.dispose();
	}
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private List<String> createInitialDataModel(List<String> displayResult) {
		
		for(int i = 0; i < displayResult.size(); i++){
			printResult.add(displayResult.get(i));
		}
		return printResult;
		
	}
}