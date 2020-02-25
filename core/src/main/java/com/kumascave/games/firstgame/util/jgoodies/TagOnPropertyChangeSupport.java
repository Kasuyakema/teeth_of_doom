package com.kumascave.games.firstgame.util.jgoodies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.common.base.Objects;
import com.jgoodies.common.bean.ObservableBean2;

public interface TagOnPropertyChangeSupport extends ObservableBean2 {

	/**
	 * Adds a PropertyChangeListener to the listener list. The listener is
	 * registered for all bound properties of this class.
	 * <p>
	 *
	 * If listener is {@code null}, no exception is thrown and no action is
	 * performed.
	 *
	 * @param listener
	 *            the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 * @see #removePropertyChangeListener(String, PropertyChangeListener)
	 * @see #addPropertyChangeListener(String, PropertyChangeListener)
	 * @see #getPropertyChangeListeners()
	 */
	@Override
	default public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (getChangeSupport() == null) {
			setChangeSupport(createPropertyChangeSupport(this));
		}
		getChangeSupport().addPropertyChangeListener(listener);
	}

	/**
	 * Removes a PropertyChangeListener from the listener list. This method should
	 * be used to remove PropertyChangeListeners that were registered for all bound
	 * properties of this class.
	 * <p>
	 *
	 * If listener is {@code null}, no exception is thrown and no action is
	 * performed.
	 *
	 * @param listener
	 *            the PropertyChangeListener to be removed
	 * @see #addPropertyChangeListener(PropertyChangeListener)
	 * @see #addPropertyChangeListener(String, PropertyChangeListener)
	 * @see #removePropertyChangeListener(String, PropertyChangeListener)
	 * @see #getPropertyChangeListeners()
	 */
	@Override
	default public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null || getChangeSupport() == null) {
			return;
		}
		getChangeSupport().removePropertyChangeListener(listener);
	}

	/**
	 * Adds a PropertyChangeListener to the listener list for a specific property.
	 * The specified property may be user-defined.
	 * <p>
	 *
	 * Note that if this Model is inheriting a bound property, then no event will be
	 * fired in response to a change in the inherited property.
	 * <p>
	 *
	 * If listener is {@code null}, no exception is thrown and no action is
	 * performed.
	 *
	 * @param propertyName
	 *            one of the property names listed above
	 * @param listener
	 *            the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener(String, PropertyChangeListener)
	 * @see #addPropertyChangeListener(String, PropertyChangeListener)
	 * @see #getPropertyChangeListeners(String)
	 */
	@Override
	default public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (getChangeSupport() == null) {
			setChangeSupport(createPropertyChangeSupport(this));
		}
		getChangeSupport().addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Removes a PropertyChangeListener from the listener list for a specific
	 * property. This method should be used to remove PropertyChangeListeners that
	 * were registered for a specific bound property.
	 * <p>
	 *
	 * If listener is {@code null}, no exception is thrown and no action is
	 * performed.
	 *
	 * @param propertyName
	 *            a valid property name
	 * @param listener
	 *            the PropertyChangeListener to be removed
	 *
	 * @see #addPropertyChangeListener(String, PropertyChangeListener)
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 * @see #getPropertyChangeListeners(String)
	 */
	@Override
	default public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null || getChangeSupport() == null) {
			return;
		}
		getChangeSupport().removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Returns an array of all the property change listeners registered on this
	 * component.
	 *
	 * @return all of this component's {@code PropertyChangeListener}s or an empty
	 *         array if no property change listeners are currently registered
	 *
	 * @see #addPropertyChangeListener(PropertyChangeListener)
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 * @see #getPropertyChangeListeners(String)
	 * @see PropertyChangeSupport#getPropertyChangeListeners()
	 */
	@Override
	default public PropertyChangeListener[] getPropertyChangeListeners() {
		if (getChangeSupport() == null) {
			return new PropertyChangeListener[0];
		}
		return getChangeSupport().getPropertyChangeListeners();
	}

	/**
	 * Returns an array of all the listeners which have been associated with the
	 * named property.
	 *
	 * @param propertyName
	 *            the name of the property to lookup listeners
	 * @return all of the {@code PropertyChangeListeners} associated with the named
	 *         property or an empty array if no listeners have been added
	 *
	 * @see #addPropertyChangeListener(String, PropertyChangeListener)
	 * @see #removePropertyChangeListener(String, PropertyChangeListener)
	 * @see #getPropertyChangeListeners()
	 */
	@Override
	default public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		if (getChangeSupport() == null) {
			return new PropertyChangeListener[0];
		}
		return getChangeSupport().getPropertyChangeListeners(propertyName);
	}

	/**
	 * Creates and returns a PropertyChangeSupport for the given bean. Invoked by
	 * the first call to {@link #addPropertyChangeListener} when lazily creating the
	 * sole change support instance used throughout this bean.
	 * <p>
	 *
	 * This default implementation creates a {@code PropertyChangeSupport}.
	 * Subclasses may override to return other change support implementations. For
	 * example to ensure that listeners are notified in the Event dispatch thread
	 * (EDT change support). The JGoodies Binding uses an extended change support
	 * that allows to configure whether the old and new value are compared with
	 * {@code ==} or {@code equals}.
	 *
	 * @param bean
	 *            the bean to create a change support for
	 * @return the new change support
	 */
	default public PropertyChangeSupport createPropertyChangeSupport(final Object bean) {
		return new PropertyChangeSupport(bean);
	}

	/**
	 * General support for reporting bound property changes. Sends the given
	 * PropertyChangeEvent to any registered PropertyChangeListener.
	 * <p>
	 *
	 * Most bean setters will invoke the fireXXX methods that get a property name
	 * and the old and new value. However some frameworks and setters may prefer to
	 * use this general method. Also, this method allows to fire
	 * IndexedPropertyChangeEvents that have been introduced in Java 5.
	 *
	 * @param event
	 *            describes the property change
	 *
	 * @since 1.3
	 */
	default public void firePropertyChange(PropertyChangeEvent event) {
		PropertyChangeSupport aChangeSupport = getChangeSupport();
		if (aChangeSupport == null || Objects.equal(event.getNewValue(), event.getOldValue())) {
			return;
		}
		aChangeSupport.firePropertyChange(event);
	}

	/**
	 * Support for reporting bound property changes for Object properties. This
	 * method can be called when a bound property has changed and it will send the
	 * appropriate PropertyChangeEvent to any registered PropertyChangeListeners.
	 *
	 * @param propertyName
	 *            the property whose value has changed
	 * @param oldValue
	 *            the property's previous value
	 * @param newValue
	 *            the property's new value
	 */
	default public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		PropertyChangeSupport aChangeSupport = getChangeSupport();
		if (aChangeSupport == null || Objects.equal(newValue, oldValue)) {
			return;
		}
		aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void setChangeSupport(PropertyChangeSupport propertyChangeSupport);

	public PropertyChangeSupport getChangeSupport();
}
