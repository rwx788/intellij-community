/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.externalSystem.action;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.ImportModuleAction;
import com.intellij.ide.util.newProjectWizard.AddModuleWizard;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.externalSystem.ExternalSystemManager;
import com.intellij.openapi.externalSystem.model.ExternalSystemDataKeys;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalModuleBuilder;
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalProjectImportProvider;
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil;
import com.intellij.openapi.externalSystem.util.ExternalSystemBundle;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.projectImport.ProjectImportProvider;

/**
 * @author Denis Zhdanov
 * @since 6/14/13 1:28 PM
 */
public class AttachExternalProjectAction extends AnAction implements DumbAware {

  public AttachExternalProjectAction() {
    getTemplatePresentation().setText(ExternalSystemBundle.message("action.attach.external.project.text"));
    getTemplatePresentation().setDescription(ExternalSystemBundle.message("action.attach.external.project.description"));
  }

  @Override
  public void update(AnActionEvent e) {
    e.getPresentation().setIcon(SystemInfoRt.isMac ? AllIcons.ToolbarDecorator.Mac.Add : AllIcons.ToolbarDecorator.Add);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    ProjectSystemId externalSystemId = ExternalSystemDataKeys.EXTERNAL_SYSTEM_ID.getData(e.getDataContext());
    if (externalSystemId == null) {
      return;
    }

    ExternalSystemManager<?,?,?,?,?> manager = ExternalSystemApiUtil.getManager(externalSystemId);
    if (manager == null) {
      return;
    }

    Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    if (project == null) {
      return;
    }
    
    ProjectImportProvider[] projectImportProviders = new ProjectImportProvider[1];
    for (ProjectImportProvider provider : ProjectImportProvider.PROJECT_IMPORT_PROVIDER.getExtensions()) {
      if (provider instanceof AbstractExternalProjectImportProvider
          && externalSystemId.equals(((AbstractExternalProjectImportProvider)provider).getExternalSystemId()))
      {
        projectImportProviders[0] = provider;
        break;
      }
    }
    if (projectImportProviders[0] == null) {
      return;
    }

    AbstractExternalModuleBuilder moduleBuilder = null;
    for (ModuleBuilder builder : ModuleBuilder.getAllBuilders()) {
      if (builder instanceof AbstractExternalModuleBuilder
          && externalSystemId.equals(((AbstractExternalModuleBuilder)builder).getExternalSystemId()))
      {
        moduleBuilder = (AbstractExternalModuleBuilder)builder;
        break;
      }
    }
    if (moduleBuilder == null) {
      return;
    }
    

    AddModuleWizard wizard = ImportModuleAction.selectFileAndCreateWizard(project,
                                                                          null,
                                                                          manager.getExternalProjectDescriptor(),
                                                                          projectImportProviders);
    if (wizard != null) {
      WizardContext wizardContext = wizard.getWizardContext();
      wizardContext.setProjectBuilder(moduleBuilder);
      ModuleWizardStep[] steps = moduleBuilder.createWizardSteps(wizardContext, ModulesProvider.EMPTY_MODULES_PROVIDER);
      for (ModuleWizardStep step : steps) {
        step.getComponent();
      }
      ImportModuleAction.createFromWizard(project, wizard);
    }
  }
}
