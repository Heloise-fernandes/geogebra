package geogebra.touch.gui.elements.header;

import geogebra.common.kernel.Kernel;
import geogebra.touch.FileManagerM;
import geogebra.touch.TouchApp;
import geogebra.touch.TouchEntryPoint;
import geogebra.touch.gui.CommonResources;
import geogebra.touch.gui.TabletGUI;
import geogebra.touch.gui.dialogs.FileDialog;
import geogebra.touch.gui.dialogs.InfoDialog;
import geogebra.touch.gui.elements.StandardImageButton;
import geogebra.touch.model.TouchModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * ButtonBar for the buttons on the left side of the HeaderPanel.
 * 
 * @author Thomas Krismayer
 * 
 */
public class TabletHeaderPanelLeft extends HorizontalPanel
{
	Kernel kernel;
	TouchApp app;
	TouchModel touchModel;
	TabletGUI tabletGUI;
	FileManagerM fm;

	FileDialog openDialog, saveDialog;
	InfoDialog infoDialog;

	private StandardImageButton newButton = new StandardImageButton(CommonResources.INSTANCE.document_new());
	private StandardImageButton openButton = new StandardImageButton(CommonResources.INSTANCE.document_open());
	private StandardImageButton saveButton = new StandardImageButton(CommonResources.INSTANCE.document_save());
	
	/**
	 * Generates the Buttons for the left HeaderPanel.
	 */
	public TabletHeaderPanelLeft(TabletGUI tabletGUI, TouchApp app, TouchModel touchModel, FileManagerM fm)
	{
		this.app = app;
		this.kernel = app.getKernel();

		this.tabletGUI = tabletGUI;
		this.touchModel = touchModel;
		this.fm = fm;

		this.infoDialog = new InfoDialog(this.app, fm, touchModel.getGuiModel());

		initNewButton();
		initOpenButton();
		initSaveButton();

		this.add(this.newButton);
		this.add(this.openButton);
		this.add(this.saveButton);
	}

	private void initNewButton()
	{
		final Runnable newConstruction = new Runnable()
		{
			@Override
			public void run()
			{
				TabletHeaderPanelLeft.this.app.getEuclidianView1().setPreview(null);
				TabletHeaderPanelLeft.this.touchModel.resetSelection();
				TabletHeaderPanelLeft.this.touchModel.getGuiModel().closeOptions();
				TabletHeaderPanelLeft.this.kernel.getApplication().getGgbApi().newConstruction();
				TabletHeaderPanelLeft.this.app.setDefaultConstructionTitle();
				TabletHeaderPanelLeft.this.kernel.notifyRepaint();
				TabletHeaderPanelLeft.this.app.setSaved();
			}
		};

		this.newButton.addDomHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				TabletHeaderPanelLeft.this.infoDialog.setCallback(newConstruction);
				TabletHeaderPanelLeft.this.infoDialog.showIfNeeded(TabletHeaderPanelLeft.this.app);
			}
		}, ClickEvent.getType());
	}

	private void initOpenButton()
	{

		final Runnable showOpenDialog = new Runnable()
		{
			@Override
			public void run()
			{
				TabletHeaderPanelLeft.this.touchModel.getGuiModel().closeOptions();
				TouchEntryPoint.showBrowseGUI();
			}
		};
		this.openButton.addDomHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				event.preventDefault();
				TabletHeaderPanelLeft.this.infoDialog.setCallback(showOpenDialog);
				TabletHeaderPanelLeft.this.infoDialog.showIfNeeded(TabletHeaderPanelLeft.this.app);
			}
		}, ClickEvent.getType());
	}

	private void initSaveButton()
	{
		this.saveButton.addDomHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				event.preventDefault();
				TabletHeaderPanelLeft.this.touchModel.getGuiModel().closeOptions();
				if(TabletHeaderPanelLeft.this.app.isDefaultFileName()
						&& TabletHeaderPanelLeft.this.app.getConstructionTitle().equals(TabletHeaderPanelLeft.this.tabletGUI.getConstructionTitle())
						){
					
				}else{
					TabletHeaderPanelLeft.this.fm.saveFile(TabletHeaderPanelLeft.this.app);
				}
			}
		}, ClickEvent.getType());
	}

	/**
	 * Sets the title in the {@link TabletHeaderPanel tabletHeader}
	 * 
	 * @param title
	 */
	@Override
	public void setTitle(String title)
	{
		// FIXME ugly, implement observer pattern!
		this.tabletGUI.getLAF().setTitle(title);
	}

	public void setLabels()
	{
		this.openDialog.setLabels();
	}

}