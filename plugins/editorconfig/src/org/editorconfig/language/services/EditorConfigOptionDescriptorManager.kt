// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.editorconfig.language.services

import com.intellij.openapi.components.ServiceManager
import com.intellij.psi.PsiElement
import org.editorconfig.language.schema.descriptors.EditorConfigDescriptor
import org.editorconfig.language.schema.descriptors.impl.EditorConfigDeclarationDescriptor
import org.editorconfig.language.schema.descriptors.impl.EditorConfigOptionDescriptor
import org.editorconfig.language.schema.descriptors.impl.EditorConfigQualifiedKeyDescriptor

interface EditorConfigOptionDescriptorManager {
  fun getOptionDescriptor(key: PsiElement, parts: List<String>, smart: Boolean): EditorConfigOptionDescriptor?
  fun getQualifiedKeys(smart: Boolean): List<EditorConfigQualifiedKeyDescriptor>
  fun getDeclarationDescriptors(id: String): List<EditorConfigDeclarationDescriptor>
  fun getSimpleKeyDescriptors(smart: Boolean): List<EditorConfigDescriptor>
  fun getRequiredDeclarationDescriptors(id: String): List<EditorConfigDeclarationDescriptor>

  companion object {
    val instance: EditorConfigOptionDescriptorManager
      get() = ServiceManager.getService(EditorConfigOptionDescriptorManager::class.java)
  }
}
