package doext.implement;

import java.util.LinkedList;

import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import core.DoServiceContainer;
import core.helper.DoJsonHelper;
import core.helper.DoUIModuleHelper;
import core.interfaces.DoIScriptEngine;
import core.interfaces.DoIUIModuleView;
import core.interfaces.datamodel.DoIAnimation;
import core.object.DoInvokeResult;
import core.object.DoProperty;
import core.object.DoUIModule;
import doext.define.do_Animator_IMethod;
import doext.define.do_Animator_MAbstract;

/**
 * 自定义扩展MM组件Model实现，继承do_Animator_MAbstract抽象类，并实现do_Animator_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.getUniqueKey());
 */
public class do_Animator_Model extends do_Animator_MAbstract implements do_Animator_IMethod, DoIAnimation {

	private LinkedList<do_Animator_Entity> animators;

	public do_Animator_Model() throws Exception {
		super();
		animators = new LinkedList<do_Animator_Entity>();
	}

	@Override
	public void loadSync(String _content) throws Exception {
		super.loadSync(_content);
		loadModel(DoJsonHelper.loadDataFromText(_content));
	}

	@Override
	public void load(String _content) throws Exception {
		super.load(_content);
		loadModel(DoJsonHelper.loadDataFromText(_content));
	}

	private void loadModel(JSONObject _dictParas) throws Exception {
		int _duration = DoJsonHelper.getInt(_dictParas, "duration", -1);
		if (_duration < 0) {
			throw new Exception("duration 未设置值或非法值！");
		}
		String _curve = DoJsonHelper.getString(_dictParas, "curve", "Linear");
		JSONObject _props = DoJsonHelper.getJSONObject(_dictParas, "props");

		if (_props != null) {
			do_Animator_Entity _entity = new do_Animator_Entity();
			if (_props.has("x")) {
				_entity.setX(DoJsonHelper.getInt(_props, "x", 0));
				_entity.setHasX(true);
			}
			if (_props.has("y")) {
				float _y = DoJsonHelper.getInt(_props, "y", 0);
				_entity.setY(_y);
				_entity.setHasY(true);
			}
			if (_props.has("width")) {
				int _width = DoJsonHelper.getInt(_props, "width", 0);
				_entity.setWidth(_width);
			}
			if (_props.has("height")) {
				int _height = DoJsonHelper.getInt(_props, "height", 0);
				_entity.setHeight(_height);
			}
			if (_props.has("bgColor")) {
				_entity.setBgColor(DoJsonHelper.getString(_props, "bgColor", null));
				_entity.setHasBgColor(true);
			}
			if (_props.has("alpha")) {
				_entity.setAlpha((float) DoJsonHelper.getDouble(_props, "alpha", 0));
				_entity.setHasAlpha(true);
			}
			_entity.setDuration(_duration);
			_entity.setCurve(_curve);
			animators.add(_entity);
		}
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("append".equals(_methodName)) {
			this.append(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 创建属性动画；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void append(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		loadModel(_dictParas);
	}

	@Override
	public void setViewAnimation(final View _view, final DoIScriptEngine _scriptEngine, final DoInvokeResult _invokeResult, final String _callbackFuncName) throws Exception {
		DoUIModule _model = ((DoIUIModuleView) _view).getModel();
		if (animators != null && animators.size() > 0) {
			startAnimator(_view, _model, _scriptEngine, _invokeResult, _callbackFuncName);
		}

	}

	private void startAnimator(View _view, final DoUIModule _model, final DoIScriptEngine _scriptEngine, final DoInvokeResult _invokeResult, final String _callbackFuncName) throws Exception {
		final AnimatorSet _animatorSet = new AnimatorSet();
		for (int i = 0; i < animators.size(); i++) {
			final do_Animator_Entity _entity = animators.get(i);
			int preI = i - 1;
			if (preI >= 0) {
				_entity.setPreviousEntity(animators.get(preI));
			}
			AnimatorSet _childAnimatorSet = createAnimator(_entity, _view, _model);
			_childAnimatorSet.addListener(new BaseAnimatorListener() {
				@Override
				public void onAnimationEnd(Animator animation) {
					// 修改view的值
					updateValue(_entity, _model);
				}
			});

			_animatorSet.play(_childAnimatorSet);
		}
		_animatorSet.addListener(new BaseAnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animation) {
				_scriptEngine.callback(_callbackFuncName, _invokeResult);
			}
		});

		DoServiceContainer.getPageViewFactory().getAppContext().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_animatorSet.start();
			}
		});
	}

	private class BaseAnimatorListener implements AnimatorListener {

		public BaseAnimatorListener() {
		}

		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {

		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}
	}

	private void updateValue(do_Animator_Entity _entity, DoUIModule _model) {
		DoIUIModuleView _uiView = _model.getCurrentUIModuleView();

		if (null != _uiView && _entity.hasX()) {
			float _x = _entity.getX();
			_model.setPropertyValue("x", _x + "");
		}
		if (null != _uiView && _entity.hasY()) {
			float _y = _entity.getY();
			_model.setPropertyValue("y", _y + "");
		}
		float _width = _entity.getWidth();
		if (null != _uiView && _width >= 0) {
			_model.setPropertyValue("width", _width + "");
		}
		float _height = _entity.getHeight();
		if (null != _uiView && _height >= 0) {
			_model.setPropertyValue("height", _height + "");
		}
		String _bgColor = _entity.getBgColor();
		if (null != _uiView && !TextUtils.isEmpty(_bgColor)) {
			_model.setPropertyValue("bgColor", _bgColor);
		}
	}

	/**
	 * 创建一个animatorSet 动画
	 * 
	 * @param _entity
	 * @param _view
	 * @return
	 * @throws Exception
	 */
	private AnimatorSet createAnimator(do_Animator_Entity _entity, final View _view, final DoUIModule _model) throws Exception {
		double _xZoom = _model.getXZoom();
		double _yZoom = _model.getYZoom();
		AnimatorSet _set = new AnimatorSet();

		do_Animator_Entity _preEntity = _entity.getPreviousEntity();

		if (_entity.hasX()) {
			int _x = DoUIModuleHelper.getCalcValue(_entity.getX() * _xZoom);
			final MarginLayoutParams params = (MarginLayoutParams) _view.getLayoutParams();
			int _starX = params.leftMargin;
			if (_preEntity != null && _preEntity.hasX()) {
				_starX = DoUIModuleHelper.getCalcValue(_preEntity.getX() * _xZoom);
			}
			ValueAnimator valueAnimator = ValueAnimator.ofInt(_starX, _x);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					params.leftMargin = (Integer) animation.getAnimatedValue();
					_view.setLayoutParams(params);
				}
			});
			_set.play(valueAnimator);
		}

		if (_entity.hasY()) {
			int _y = DoUIModuleHelper.getCalcValue(_entity.getY() * _yZoom);
			final MarginLayoutParams params = (MarginLayoutParams) _view.getLayoutParams();
			int _starY = params.topMargin;
			if (_preEntity != null && _preEntity.hasY()) {
				_starY = DoUIModuleHelper.getCalcValue(_preEntity.getY() * _yZoom);
			}
			ValueAnimator valueAnimator = ValueAnimator.ofInt(_starY, _y);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					params.topMargin = (Integer) animation.getAnimatedValue();
					_view.setLayoutParams(params);
				}
			});
			_set.play(valueAnimator);
		}

		if (_entity.hasAlpha()) {
			float _alpha = _entity.getAlpha();
			float _starAlpha = _view.getAlpha();
			if (_preEntity != null && _preEntity.hasAlpha()) {
				_starAlpha = _preEntity.getAlpha();
			}
			ValueAnimator valueAnimator = ValueAnimator.ofFloat(_starAlpha, _alpha);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					_view.setAlpha((Float) animation.getAnimatedValue());
				}
			});
			_set.play(valueAnimator);
		}

		int _width = _entity.getWidth();
		if (_width >= 0) {
			_width = DoUIModuleHelper.getCalcValue(_width * _xZoom);
			final LayoutParams params = _view.getLayoutParams();
			int _starWidth = params.width;
			if (_preEntity != null) {
				int _preWidth = _preEntity.getWidth();
				if (_preWidth != -1) {//有可能上一个动画没有包含 Width
					_starWidth = DoUIModuleHelper.getCalcValue(_preWidth * _xZoom);
				}
			}
			ValueAnimator valueAnimator = ValueAnimator.ofInt(_starWidth, _width);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					params.width = (Integer) animation.getAnimatedValue();
					_view.setLayoutParams(params);
				}
			});
			_set.play(valueAnimator);
		}

		int _height = _entity.getHeight();
		if (_height >= 0) {
			_height = DoUIModuleHelper.getCalcValue(_height * _yZoom);
			final LayoutParams params = _view.getLayoutParams();
			int _starHeight = params.height;
			if (_preEntity != null) {
				int _preHeight = _preEntity.getHeight();
				if (_preHeight != -1) { //有可能上一个动画没有包含 Height
					_starHeight = DoUIModuleHelper.getCalcValue(_preHeight * _yZoom);
				}
			}
			ValueAnimator valueAnimator = ValueAnimator.ofInt(_starHeight, _height);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					params.height = (Integer) animation.getAnimatedValue();
					_view.setLayoutParams(params);
				}
			});
			_set.play(valueAnimator);
		}

		final String _bgColor = _entity.getBgColor();
		if (null != _bgColor) {
			int _colorFrom = Color.TRANSPARENT;
			if (_preEntity != null && _preEntity.hasBgColor()) {
				_colorFrom = DoUIModuleHelper.getColorFromString(_preEntity.getBgColor(), _colorFrom);
			} else {
				DoProperty bgColorProperty = _model.getProperty("bgColor");
				if (null != bgColorProperty) {
					_colorFrom = DoUIModuleHelper.getColorFromString(bgColorProperty.getValue(), _colorFrom);
				} else {
					Drawable _drawable = _view.getBackground();
					if (_drawable instanceof ColorDrawable) {
						_colorFrom = ((ColorDrawable) _drawable).getColor();
					}
				}
			}

			int _colorTo = DoUIModuleHelper.getColorFromString(_bgColor, _colorFrom);

			ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), _colorFrom, _colorTo);
			colorAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					DoUIModuleHelper.setBgColorAndBorder(_model, _view, (Integer) animator.getAnimatedValue());
				}
			});
			colorAnimator.addListener(new BaseAnimatorListener() {
				@Override
				public void onAnimationEnd(Animator animation) {
					_model.setPropertyValue("bgColor", _bgColor);
				}
			});
			_set.play(colorAnimator);
		}

		_set.setDuration(_entity.getDuration());
		Interpolator _interpolator = _entity.getCurve();
		if (null != _interpolator) {
			_set.setInterpolator(_interpolator);
		}
		_set.setStartDelay(_entity.getDelay());
		return _set;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}