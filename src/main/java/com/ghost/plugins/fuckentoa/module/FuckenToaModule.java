/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * Copyright (c) 2022, LlemonDuck
 * Copyright (c) 2022, TheStonedTurtle
 * Copyright (c) 2019, Ron Young <https://github.com/raiyni>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ghost.plugins.fuckentoa.module;

import com.ghost.plugins.fuckentoa.FuckenToaConfig;
import com.ghost.plugins.fuckentoa.boss.akkha.*;
import com.ghost.plugins.fuckentoa.boss.baba.Baba;
import com.ghost.plugins.fuckentoa.boss.baba.BabaSceneOverlay;
import com.ghost.plugins.fuckentoa.boss.kephri.Kephri;
import com.ghost.plugins.fuckentoa.boss.kephri.KephriSceneOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase2.WardenP2;
import com.ghost.plugins.fuckentoa.boss.warden.phase2.WardenP2PrayerInfoboxOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase2.WardenP2PrayerWidgetOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase2.WardenP2SceneOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase3.WardenP3;
import com.ghost.plugins.fuckentoa.boss.warden.phase3.WardenP3PrayerInfoboxOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase3.WardenP3PrayerWidgetOverlay;
import com.ghost.plugins.fuckentoa.boss.warden.phase3.WardenP3SceneOverlay;
import com.ghost.plugins.fuckentoa.boss.zebak.Zebak;
import com.ghost.plugins.fuckentoa.boss.zebak.ZebakPrayerInfoboxOverlay;
import com.ghost.plugins.fuckentoa.boss.zebak.ZebakPrayerWidgetOverlay;
import com.ghost.plugins.fuckentoa.boss.zebak.ZebakSceneOverlay;
import com.ghost.plugins.fuckentoa.challenge.QuickProceedSwaps;
import com.ghost.plugins.fuckentoa.challenge.apmeken.Apmeken;
import com.ghost.plugins.fuckentoa.challenge.apmeken.ApmekenOverlay;
import com.ghost.plugins.fuckentoa.challenge.het.Het;
import com.ghost.plugins.fuckentoa.challenge.het.HetOverlay;
import com.ghost.plugins.fuckentoa.challenge.scabaras.*;
import com.ghost.plugins.fuckentoa.hud.FadeDisabler;
import com.ghost.plugins.fuckentoa.hud.HpOrbManager;
import com.ghost.plugins.fuckentoa.nexus.PathLevelTracker;
import com.ghost.plugins.fuckentoa.pointstracker.PointsTracker;
import com.ghost.plugins.fuckentoa.tomb.SarcophagusRecolorer;
import com.ghost.plugins.fuckentoa.util.RaidStateTracker;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

import javax.inject.Singleton;

@Slf4j
public class FuckenToaModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		final Multibinder<PluginLifecycleComponent> lifecycleComponents =
			Multibinder.newSetBinder(binder(), PluginLifecycleComponent.class);

		lifecycleComponents.addBinding().to(RaidStateTracker.class);
		lifecycleComponents.addBinding().to(PathLevelTracker.class);

		lifecycleComponents.addBinding().to(PointsTracker.class);

		lifecycleComponents.addBinding().to(Baba.class);
		lifecycleComponents.addBinding().to(BabaSceneOverlay.class);

		lifecycleComponents.addBinding().to(Kephri.class);
		lifecycleComponents.addBinding().to(KephriSceneOverlay.class);

		lifecycleComponents.addBinding().to(Akkha.class);
		lifecycleComponents.addBinding().to(AkkhaSceneOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaPrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaPrayerInfoboxOverlay.class);
		lifecycleComponents.addBinding().to(AkkhaMemoryBlast.class);
		lifecycleComponents.addBinding().to(AkkhaFinalStand.class);

		lifecycleComponents.addBinding().to(Zebak.class);
		lifecycleComponents.addBinding().to(ZebakSceneOverlay.class);
		lifecycleComponents.addBinding().to(ZebakPrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(ZebakPrayerInfoboxOverlay.class);

		lifecycleComponents.addBinding().to(WardenP2.class);
		lifecycleComponents.addBinding().to(WardenP2SceneOverlay.class);
		lifecycleComponents.addBinding().to(WardenP2PrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(WardenP2PrayerInfoboxOverlay.class);

		lifecycleComponents.addBinding().to(WardenP3.class);
		lifecycleComponents.addBinding().to(WardenP3SceneOverlay.class);
		lifecycleComponents.addBinding().to(WardenP3PrayerWidgetOverlay.class);
		lifecycleComponents.addBinding().to(WardenP3PrayerInfoboxOverlay.class);

		lifecycleComponents.addBinding().to(Apmeken.class);
		lifecycleComponents.addBinding().to(ApmekenOverlay.class);

		lifecycleComponents.addBinding().to(ScabarasAdditionPuzzle.class);
		lifecycleComponents.addBinding().to(ScabarasLightPuzzle.class);
		lifecycleComponents.addBinding().to(ScabarasMatchingPuzzle.class);
		lifecycleComponents.addBinding().to(ScabarasObeliskPuzzle.class);
		lifecycleComponents.addBinding().to(ScabarasSequencePuzzle.class);
		lifecycleComponents.addBinding().to(ScabarasOverlay.class);

		lifecycleComponents.addBinding().to(Het.class);
		lifecycleComponents.addBinding().to(HetOverlay.class);

		lifecycleComponents.addBinding().to(FadeDisabler.class);
		lifecycleComponents.addBinding().to(HpOrbManager.class);
		lifecycleComponents.addBinding().to(QuickProceedSwaps.class);
		lifecycleComponents.addBinding().to(SarcophagusRecolorer.class);
	}

	@Provides
	@Singleton
	FuckenToaConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(FuckenToaConfig.class);
	}

}
