package at.ac.tuwien.tiss.util.fe.jsf;

import org.apache.commons.collections.MapUtils;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Copied from the jShowcase
 */
public class JsfUtil {
    public JsfUtil() {
    }

    public static String getUIComponent(String componentId) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        UIComponent c = getUIComponent(root, componentId);
        return c != null ? c.getClientId(context) : null;
    }

    public static UIComponent getUIComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        } else {
            Iterator kids = c.getFacetsAndChildren();

            UIComponent found;
            do {
                if (!kids.hasNext()) {
                    return null;
                }

                found = getUIComponent((UIComponent) kids.next(), id);
            } while (found == null);

            return found;
        }
    }

    public static ClassLoader getCurrentClassLoader(Object o) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null && o != null) {
            loader = o.getClass().getClassLoader();
        }

        return loader;
    }

    public static String getMessageResourceString(String bundleName, Locale locale, String key, Object... params) {
        String text = null;
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(key));

        try {
            text = bundle.getString(key);
        } catch (MissingResourceException var7) {
            text = "????? " + key + " ?????";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params, new StringBuffer(), null).toString();
        }

        return text;
    }

    public static void addStatusMessage(Severity severity, String msg) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(severity, msg, null));
    }

    public static void addStatusMessage(Severity severity, String bundleName, Locale locale, String key, Object... params) {
        addStatusMessage(severity, getMessageResourceString(bundleName, locale, key, params));
    }

    public static void addMessageForComponent(String componentId, Severity severity, String msg) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(severity, msg, null);
        String compId = getUIComponent(componentId);
        facesContext.addMessage(compId, message);
    }

    public static void addMessageForComponent(String componentId, Severity severity, String bundleName, String key, Locale locale, Object... params) {
        addMessageForComponent(componentId, severity, getMessageResourceString(bundleName, locale, key, params));
    }

    public static void externalRedirect(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ctx = fc.getExternalContext();
        if (!ctx.isResponseCommitted()) {
            try {
                ctx.redirect(url);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }

            fc.responseComplete();
        }

    }

    public static void sendError(int statusCode) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().responseSendError(statusCode, null);
            facesContext.responseComplete();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void navigate(String outcome) {
        navigate(outcome, false, false, null);
    }

    public static void navigate(String outcome, boolean facesRedirect, boolean includeViewParams) {
        navigate(outcome, facesRedirect, includeViewParams, null);
    }

    public static void navigate(String outcome, boolean facesRedirect, boolean includeViewParams, Map<String, String> additionalParams) {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, createOutcome(outcome, facesRedirect, includeViewParams, additionalParams));
        fc.renderResponse();
    }

    public static String createOutcome(String viewId, boolean facesRedirect, boolean includeViewParams) {
        return createOutcome(viewId, facesRedirect, includeViewParams, null);
    }

    public static String createOutcome(String viewId, boolean facesRedirect, boolean includeViewParams, Map<String, String> additionalParams) {
        Map<String, String> params = new HashMap();
        if (facesRedirect) {
            params.put("faces-redirect", "true");
            if (includeViewParams) {
                params.put("includeViewParams", "true");
            }
        }

        if (MapUtils.isNotEmpty(additionalParams)) {
            params.putAll(additionalParams);
        }

        StringBuilder outcome = new StringBuilder(viewId);
        String separator = "?";

        for (Iterator var7 = params.entrySet().iterator(); var7.hasNext(); separator = "&") {
            Entry<String, String> entry = (Entry) var7.next();
            outcome.append(separator).append(entry.getKey()).append("=").append(entry.getValue());
        }

        return outcome.toString();
    }

    public static boolean isAjaxRequest() {
        return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
    }
}
