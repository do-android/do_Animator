package doext.implement;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class do_Animator_Entity {

	private float x;
	private float y;
	private int width = -1;
	private int height = -1;
	private String bgColor;
	private float alpha;

	private boolean hasX;
	private boolean hasY;
	private boolean hasAlpha;
	private boolean hasBgColor;

	private int duration;
	private Interpolator curve;

	private int delay;

	private do_Animator_Entity previousEntity;

	public int getDelay() {
		if (previousEntity == null) {
			delay = 0;
		} else {
			delay = previousEntity.delay + previousEntity.duration;
		}
		return delay;
	}

	public void setPreviousEntity(do_Animator_Entity entity) {
		this.previousEntity = entity;
	}

	public do_Animator_Entity getPreviousEntity() {
		return this.previousEntity;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Interpolator getCurve() {
		return curve;
	}

	public boolean hasX() {
		return hasX;
	}

	public void setHasX(boolean hasX) {
		this.hasX = hasX;
	}

	public boolean hasY() {
		return hasY;
	}

	public void setHasY(boolean hasY) {
		this.hasY = hasY;
	}

	public boolean hasBgColor() {
		return hasBgColor;
	}

	public void setHasBgColor(boolean hasBgColor) {
		this.hasBgColor = hasBgColor;
	}

	public boolean hasAlpha() {
		return hasAlpha;
	}

	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	public void setCurve(String curve) {
		Interpolator i = null;
		if (curve != null && "EaseIn".equals(curve)) { // EaseIn动画启动的时候慢
			i = new AccelerateInterpolator();
		} else if (curve != null && "EaseOut".equals(curve)) { // EaseOut动画结束的时候慢
			i = new DecelerateInterpolator();
		} else if (curve != null && "Linear".equals(curve)) { // Linear动画速度不变
			i = new LinearInterpolator();
		} else { // EaseInOut 动画启动时候慢，中间快，结束的时候慢
			i = new AccelerateDecelerateInterpolator();
		}
		this.curve = i;
	}

}
