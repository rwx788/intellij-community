// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.psi;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a list of expressions separated by commas.
 *
 * @see PsiCall#getArgumentList()
 * @see PsiExpressionListStatement#getExpressionList()
 * @see PsiSwitchLabelStatementBase#getCaseValues()
 */
public interface PsiExpressionList extends PsiElement {
  /**
   * Returns the expressions contained in the list.
   */
  @NotNull PsiExpression[] getExpressions();

  @NotNull PsiType[] getExpressionTypes();

  /**
   * @return number of expressions in the expression list
   * @since 2018.1
   */
  default int getExpressionCount() {
    return getExpressions().length;
  }

  /**
   * @return {@code }true} if expression list contains no expressions
   * @since 2018.1
   */
  default boolean isEmpty() {
    return getExpressionCount() == 0;
  }
}