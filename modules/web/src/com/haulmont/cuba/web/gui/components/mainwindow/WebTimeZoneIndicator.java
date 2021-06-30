/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.mainwindow.TimeZoneIndicator;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.TimeZone;

public class WebTimeZoneIndicator extends WebAbstractComponent<Label> implements TimeZoneIndicator {

    protected static final String USER_TIMEZONE_LABEL_STYLENAME = "c-user-timezone-label";

    public WebTimeZoneIndicator() {
        component = new Label();
        component.setSizeUndefined();
        component.setStyleName(USER_TIMEZONE_LABEL_STYLENAME);
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        super.setBeanLocator(beanLocator);

        UserSessionSource uss = beanLocator.get(UserSessionSource.NAME);
        TimeZone timeZone = uss.getUserSession().getTimeZone();
        if (timeZone == null) {
            timeZone = getUserTimeZone(uss.getUserSession().getCurrentOrSubstitutedUser());
        }
        TimeZones timeZones = beanLocator.get(TimeZones.NAME);
        component.setValue(timeZones.getDisplayNameShort(timeZone));
        if (timeZone == null) {
            // hidden by default if timeZone is null
            setVisible(false);
        }
    }

    @Nullable
    private TimeZone getUserTimeZone(User user) {
        if (Strings.isNullOrEmpty(user.getTimeZone())) {
            if (user.getTimeZoneAuto()) {
                return TimeZone.getDefault();
            }
        } else {
            return TimeZone.getTimeZone(user.getTimeZone());
        }
        return null;
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(USER_TIMEZONE_LABEL_STYLENAME, ""));
    }

    @Override
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        component.addStyleName(USER_TIMEZONE_LABEL_STYLENAME);
    }
}