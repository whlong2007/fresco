/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.fresco.vito.provider.impl.kotlin

import com.facebook.callercontext.CallerContextVerifier
import com.facebook.fresco.vito.core.FrescoController2
import com.facebook.fresco.vito.core.FrescoVitoConfig
import com.facebook.fresco.vito.core.FrescoVitoPrefetcher
import com.facebook.fresco.vito.core.ImagePipelineUtils
import com.facebook.fresco.vito.core.VitoImagePipeline
import com.facebook.fresco.vito.core.impl.FrescoVitoPrefetcherImpl
import com.facebook.fresco.vito.core.impl.KFrescoController
import com.facebook.fresco.vito.core.impl.VitoImagePipelineImpl
import com.facebook.fresco.vito.provider.FrescoVitoProvider
import com.facebook.fresco.vito.provider.impl.NoOpCallerContextVerifier
import com.facebook.imagepipeline.core.ImagePipeline
import java.util.concurrent.Executor

class KFrescoVitoProvider(
    private val vitoConfig: FrescoVitoConfig,
    private val frescoImagePipeline: ImagePipeline,
    private val imagePipelineUtils: ImagePipelineUtils,
    private val uiThreadExecutor: Executor,
    private val lightweightBackgroundExecutor: Executor,
    private val callerContextVerifier: CallerContextVerifier = NoOpCallerContextVerifier(),
) : FrescoVitoProvider.Implementation {

  private val _imagePipeline: VitoImagePipeline by lazy {
    VitoImagePipelineImpl(frescoImagePipeline, imagePipelineUtils)
  }

  private val _controller: FrescoController2 by lazy {
    KFrescoController(
        _imagePipeline,
        uiThreadExecutor,
        lightweightBackgroundExecutor,
    )
  }

  private val _prefetcher: FrescoVitoPrefetcher by lazy {
    FrescoVitoPrefetcherImpl(frescoImagePipeline, imagePipelineUtils, callerContextVerifier)
  }

  override fun getController(): FrescoController2 = _controller

  override fun getPrefetcher(): FrescoVitoPrefetcher = _prefetcher

  override fun getImagePipeline(): VitoImagePipeline = _imagePipeline

  override fun getConfig(): FrescoVitoConfig = vitoConfig
}