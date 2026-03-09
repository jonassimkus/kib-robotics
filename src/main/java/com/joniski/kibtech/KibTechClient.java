package com.joniski.kibtech;

import com.joniski.kibtech.entity.ModEntities;
import com.joniski.kibtech.entity.client.WoodRobotModel;
import com.joniski.kibtech.entity.client.WoodRobotRenderer;
import com.joniski.kibtech.entity.custom.WoodRobotEntity;
import com.joniski.kibtech.menus.ModMenus;
import com.joniski.kibtech.menus.custom.BatteryChargerScreen;
import com.joniski.kibtech.menus.custom.RobotScreen;
import com.joniski.kibtech.menus.custom.SolarPanelScreen;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(value = KibTech.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = KibTech.MODID, value = Dist.CLIENT)
public class KibTechClient {
    public KibTechClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.WOOD_ROBOT.get(), WoodRobotRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event){
        event.register(ModMenus.SOLAR_PANEL_MENU.get(), SolarPanelScreen::new);
        event.register(ModMenus.BATTERY_CHARGER_MENU.get(), BatteryChargerScreen::new);
        event.register(ModMenus.ROBOT_MENU.get(), RobotScreen::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(WoodRobotModel.LAYER_LOCATION, WoodRobotModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.WOOD_ROBOT.get(), WoodRobotEntity.createAttributes().build());
    }
}

