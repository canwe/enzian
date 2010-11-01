/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks.ui;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.jolira.enzian.app.EnzianBase;
import com.jolira.enzian.tasks.Task;
import com.jolira.enzian.tasks.TaskExecutor;

/**
 * @author jfk
 * @date Oct 31, 2010 12:26:05 AM
 * @since 1.0
 */
public class TasksViewer extends EnzianBase {
    /**
     * Create a new viewer
     * 
     * @param executor
     *            the executor to be displayed
     */
    @Inject
    public TasksViewer(final TaskExecutor executor) {
        final IModel<? extends List<? extends Task>> mdl = new LoadableDetachableModel<List<Task>>() {
            private static final long serialVersionUID = -2820663100472235781L;

            @Override
            protected List<Task> load() {
                final Task[] tasks = executor.getTasks();

                return Arrays.asList(tasks);
            }
        };
        final ListView<Task> taskView = new PropertyListView<Task>("task", mdl) {
            private static final long serialVersionUID = -7625713403597488210L;

            @Override
            protected void populateItem(final ListItem<Task> item) {
                item.add(new Label("name"));
                item.add(new Label("status"));
                item.add(new Label("progress", "N/A"));
                item.add(new Link<Void>("cancel") {
                    private static final long serialVersionUID = 2527181238727006875L;

                    @Override
                    public void onClick() {
                        final Task task = item.getModelObject();

                        task.cancel();
                    }
                });
            }
        };

        add(taskView);
    }
}
