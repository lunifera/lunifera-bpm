package org.lunifera.bpm.drools.ui.vaadin;

import java.util.List;

import org.lunifera.ecview.core.common.context.I18nUtil;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.data.INestedPropertyAble;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.server.Resource;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class AbstractBPMField<M> extends CustomField<M> {

	protected final II18nService i18nService;
	protected final IResourceProvider resourceProvider;
	protected final boolean createButtonBar;
	protected Table table;

	public AbstractBPMField(II18nService i18nService,
			IResourceProvider resourceProvider, boolean createButtonbar) {
		this.i18nService = i18nService;
		this.resourceProvider = resourceProvider;
		this.createButtonBar = createButtonbar;
	}

	/**
	 * Applies the column setting to the table.
	 */
	protected void applyColumns(List<String> columns) {
		// add nested properties
		if (table.getContainerDataSource() instanceof INestedPropertyAble) {
			INestedPropertyAble<?> container = (INestedPropertyAble<?>) table
					.getContainerDataSource();
			for (String property : columns) {
				if (property.contains(".")) {
					container.addNestedContainerProperty(property);
				}
			}
		}

		table.setVisibleColumns(columns.toArray(new Object[columns.size()]));
		table.setColumnCollapsingAllowed(true);

		// traverse the columns again and set other properties
		for (String column : columns) {

			String fqnColumnId = toFqnColumnId(column);

			table.setColumnHeader(column, getColumnHeader(fqnColumnId));
			table.setColumnAlignment(column, toAlign(fqnColumnId));
			table.setColumnCollapsed(column, isCollapsed(fqnColumnId));
			table.setColumnCollapsible(column, isCollapsible(fqnColumnId));
			if (getExpandRatio(fqnColumnId) >= 0) {
				table.setColumnExpandRatio(column, getExpandRatio(fqnColumnId));
			}

			if (getIcon(fqnColumnId) != null) {
				table.setColumnIcon(column, getIcon(fqnColumnId));
			}
		}
	}

	protected abstract String toFqnColumnId(String column);

	protected Resource getIcon(String fqnColumnId) {
		String i18nKey = I18nUtil.getImageKey(fqnColumnId);
		String imageURL = null;
		if (i18nService != null) {
			imageURL = i18nService.getValue(i18nKey, UI.getCurrent()
					.getLocale());
		} else {
			imageURL = fqnColumnId;
		}

		return imageURL == null || imageURL.equals("") ? null
				: resourceProvider.getResource(imageURL);
	}

	protected int getExpandRatio(String fqnColumnId) {
		return -1;
	}

	protected boolean isCollapsible(String fqnColumnId) {
		return true;
	}

	protected Align toAlign(String fqnColumnId) {
		return Align.LEFT;
	}

	protected boolean isCollapsed(String fqnColumnId) {
		return false;
	}

	protected boolean isNestedColumn(String column) {
		return column != null && column.contains(".");
	}

	/**
	 * Returns the column header
	 * 
	 * @param column
	 * @return
	 */
	protected String getColumnHeader(String column) {
		return translate(column);
	}

	protected String translate(String i18nKey) {
		if (i18nService != null) {
			return i18nService.getValue(i18nKey, getLocale());
		} else {
			return i18nKey;
		}
	}
}
