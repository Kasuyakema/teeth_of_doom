package com.kumascave.games.firstgame.util.jgoodies;

import com.jgoodies.binding.value.AbstractValueModel;

public class VHolder<T> extends AbstractValueModel {
	private static final long serialVersionUID = -2498301504320535336L;

	/**
	 * Holds a value of type {@code Object} that is to be observed.
	 */
	private T value;

	/**
	 * Describes whether a value change event shall be fired if the old and new
	 * value are different. If {@code true} the old and new value are compared with
	 * {@code ==}. If {@code false} the values are compared with {@code #equals}.
	 *
	 * @see #setValue(Object, boolean)
	 * @see com.jgoodies.binding.beans.Model#firePropertyChange(String, Object,
	 *      Object, boolean)
	 * @see com.jgoodies.binding.beans.ExtendedPropertyChangeSupport
	 */
	private boolean checkIdentity;

	// Instance Creation ****************************************************

	/**
	 * Constructs a {@code ValueHolder} with {@code null} as initial value.
	 */
	public VHolder() {
		this(null);
	}

	/**
	 * Constructs a {@code ValueHolder} with the given initial value. By default the
	 * old and new value are compared using {@code #equals} when firing value change
	 * events.
	 *
	 * @param initialValue
	 *            the initial value
	 */
	public VHolder(T initialValue) {
		this(initialValue, false);
	}

	/**
	 * Constructs a {@code ValueHolder} with the given initial value.
	 *
	 * @param initialValue
	 *            the initial value
	 * @param checkIdentity
	 *            true to compare the old and new value using {@code ==}, false to
	 *            use {@code #equals}
	 */
	public VHolder(T initialValue, boolean checkIdentity) {
		value = initialValue;
		this.checkIdentity = checkIdentity;
	}

	// ValueModel Implementation ********************************************

	/**
	 * Returns the observed value.
	 *
	 * @return the observed value
	 */
	@Override
	public T getValue() {
		return value;
	}

	/**
	 * Sets a new value. Fires a value change event if the old and new value differ.
	 * The difference is tested with {@code ==} if {@code isIdentityCheckEnabled}
	 * answers {@code true}. The values are compared with {@code #equals} if the
	 * identity check is disabled.
	 *
	 * @param newValue
	 *            the new value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object newValue) {
		setValue((T) newValue, isIdentityCheckEnabled());
	}

	// Optional Support for Firing Events on Identity Change ***************

	/**
	 * Answers whether this ValueHolder fires value change events if and only if the
	 * old and new value are not the same.
	 *
	 * @return {@code true} if the old and new value are compared using {@code ==},
	 *         {@code false} if the values are compared using {@code #equals}
	 */
	public boolean isIdentityCheckEnabled() {
		return checkIdentity;
	}

	/**
	 * Sets the comparison that is used to check differences between the old and new
	 * value when firing value change events. This is the default setting that is
	 * used when changing the value via {@code #setValue(Object)}. You can override
	 * this default setting by changing a value via
	 * {@code #setValue(Object, boolean)}.
	 *
	 * @param checkIdentity
	 *            true to compare the old and new value using {@code ==}, false to
	 *            use {@code #equals}
	 */
	public void setIdentityCheckEnabled(boolean checkIdentity) {
		this.checkIdentity = checkIdentity;
	}

	/**
	 * Sets a new value. Fires a value change event if the old and new value differ.
	 * The difference is tested with {@code ==} if {@code checkIdentity} is
	 * {@code true}. The values are compared with {@code #equals} if the
	 * {@code checkIdentiy} parameter is set to {@code false}.
	 * <p>
	 *
	 * Unlike general bean property setters, this method does not fire an event if
	 * the old and new value are {@code null}.
	 *
	 * @param newValue
	 *            the new value
	 * @param checkIdentity
	 *            true to compare the old and new value using {@code ==}, false to
	 *            use {@code #equals}
	 */
	public void setValue(T newValue, boolean checkIdentity) {
		Object oldValue = getValue();
		if (oldValue == newValue) {
			return;
		}
		value = newValue;
		fireValueChange(oldValue, newValue, checkIdentity);
	}

}
