package org.apache.geronimo.testsuite.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Widget;

import abbot.swt.finder.WidgetFinder;
import abbot.swt.finder.WidgetFinderImpl;
import abbot.swt.finder.WidgetHierarchy;
import abbot.swt.finder.WidgetHierarchyImpl;
import abbot.swt.finder.generic.Matcher;
import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;
import abbot.swt.hierarchy.Visitable.Visitor;

public class MultiWidgetFinderImpl extends WidgetFinderImpl {
	private final WidgetHierarchy hierarchy;
	private static MultiWidgetFinderImpl Default;
	
	public MultiWidgetFinderImpl(WidgetHierarchy hierarchy) {
		super(hierarchy);
		this.hierarchy = hierarchy;
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized MultiWidgetFinderImpl getDefault() {
		if (Default == null)
			Default = new MultiWidgetFinderImpl(WidgetHierarchyImpl.getDefault());
		return Default;
	}
	
	public List<Widget> findMulti(final Matcher<Widget> matcher) throws NotFoundException,
		MultipleFoundException {
		
		// Collect matches.
		final List<Widget> found = new ArrayList<Widget>();
		hierarchy.accept(new Visitor<Widget>() {
			public Result visit(Widget candidate) {
				if (matcher.matches(candidate))
					found.add(candidate);
				return Result.ok;
			}
		});
		
		return found;
	}
	
	public List<Widget> findMulti(Widget widget, final Matcher<Widget> matcher) throws NotFoundException,
		MultipleFoundException {
		
		// Collect matches.
		final List<Widget> found = new ArrayList<Widget>();
		hierarchy.accept(widget, new Visitor<Widget>() {
			public Result visit(Widget candidate) {
				if (matcher.matches(candidate))
					found.add(candidate);
				return Result.ok;
			}
		});
		
		return found;
	}

}
