package io.wispforest.uwu.client;

import io.wispforest.owo.config.ui.ConfigScreen;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import io.wispforest.uwu.Uwu;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class SelectUwuScreenScreen extends BaseOwoScreen<FlowLayout> {

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        this.uiAdapter.rootComponent.surface(Surface.flat(0x77000000))
                .verticalAlignment(VerticalAlignment.CENTER)
                .horizontalAlignment(HorizontalAlignment.CENTER);

        this.uiAdapter.rootComponent.child(
                Components.label(Text.literal("Available screens"))
                        .shadow(true)
                        .margins(Insets.bottom(5))
        );

        var panel = Containers.verticalFlow(Sizing.content(), Sizing.content()).<FlowLayout>configure(layout -> {
            layout.padding(Insets.of(5))
                    .surface(Surface.PANEL)
                    .horizontalAlignment(HorizontalAlignment.CENTER);
        });

        panel.child(Components.button(Text.literal("code demo"), button -> this.client.setScreen(new ComponentTestScreen())).margins(Insets.vertical(3)));
        panel.child(Components.button(Text.literal("xml demo"), button -> this.client.setScreen(new TestParseScreen())).margins(Insets.vertical(3)));
        panel.child(Components.button(Text.literal("code config"), button -> this.client.setScreen(new TestConfigScreen())).margins(Insets.vertical(3)));
        panel.child(Components.button(Text.literal("xml config"), button -> this.client.setScreen(ConfigScreen.create(Uwu.CONFIG, null))).margins(Insets.vertical(3)));
        panel.child(Components.button(Text.literal("optimization test"), button -> this.client.setScreen(new TooManyComponentsScreen())).margins(Insets.vertical(3)));
        panel.child(Components.button(Text.literal("focus cycle test"), button -> this.client.setScreen(new BaseUIModelScreen<>(FlowLayout.class, BaseUIModelScreen.DataSource.file("../src/testmod/resources/assets/uwu/owo_ui/focus_cycle_test.xml")) {
            @Override
            protected void build(FlowLayout rootComponent) {}
        })).margins(Insets.vertical(3)));

        this.uiAdapter.rootComponent.child(panel);
    }
}
