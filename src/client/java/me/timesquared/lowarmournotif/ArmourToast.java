package me.timesquared.lowarmournotif;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ArmourToast implements Toast {
	private final List<OrderedText> message;
	private final int width;
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public ArmourToast(String message) {
		this.width = Math.min(240, mc.textRenderer.getWidth(message) + 16);
		this.message = this.wrap(List.of(Text.of(message)));
	}
	
	public static void notify(String message) {
		mc.getToastManager().add(new ArmourToast(message));
	}
	
	@Override
	public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
		DrawableHelper.fill(matrices, 0, 0, this.getWidth(), this.getHeight(), 0x25252525);
		int xOffset = this.getWidth() / 2 - mc.textRenderer.getWidth(this.message.get(0)) / 2;
		mc.textRenderer.drawWithShadow(matrices, this.message.get(0), xOffset, 4, 0xFFFFFF);
		
		for (int i = 1; i < this.message.size(); i++) {
			mc.textRenderer.drawWithShadow(matrices, this.message.get(i), 4, i * 11, 0xFFFFFF);
		}
		
		return startTime > 5000 ? Visibility.HIDE : Visibility.SHOW;
	}
	
	@Override
	public Object getType() {
		return Type.INFO;
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return 6 + this.message.size() * 11;
	}
	private List<OrderedText> wrap(List<Text> message) {
		var list = new ArrayList<OrderedText>();
		for (var text : message) list.addAll(mc.textRenderer.wrapLines(text, this.getWidth() - 8));
		return list;
	}
	
	enum Type {
		INFO,
	}
}
