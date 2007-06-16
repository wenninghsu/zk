/* DHtmlLayoutFilter.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Wed Nov 30 00:49:59     2005, Created by tomyeh
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.http;

import java.io.StringWriter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Strings;
import org.zkoss.util.logging.Log;
import org.zkoss.web.servlet.http.HttpBufferedResponse;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.UiFactory;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.sys.RequestInfo;
import org.zkoss.zk.ui.impl.RequestInfoImpl;

/**
 * Used to post-process the response into a ZK page.
 * It is useful if you have a servlet using servlets, JSP or other
 * technology.
 *
 * <p>How to use:
 * <ul>
 * <li>Decorates the servlet to embed the ZK components and
 * attributes, such as zk:onClick="...".</li>
 * <li>Maps the servlet to the filter in web.xml</li>
 * </ul>
 *
 * @author tomyeh
 */
public class DHtmlLayoutFilter implements Filter {
	private static final Log log = Log.lookup(DHtmlLayoutFilter.class);

	private ServletContext _ctx;
	private String _ext = "html";
	private String _charset = "UTF-8";

	private void process(HttpServletRequest request,
	HttpServletResponse response, String content)
	throws ServletException, IOException {
		if (log.debugable()) log.debug("Content to filter:\n"+content);

		final WebManager webman = WebManager.getWebManager(_ctx);
		final WebApp wapp = webman.getWebApp();
		final WebAppCtrl wappc = (WebAppCtrl)wapp;
		final Session sess = webman.getSession(_ctx, request);
		final Object old = I18Ns.setup(sess, request, response, _charset);
		try {
			final Desktop desktop = webman.getDesktop(sess, request, null, true);
			final RequestInfo ri = new RequestInfoImpl(
				wapp, sess, desktop, request, null);
			final UiFactory uf = wappc.getUiFactory();
			final PageDefinition pagedef =
				uf.getPageDefinitionDirectly(ri, content, _ext);

			final Page page = uf.newPage(ri, pagedef, null);
			final Execution exec =
				new ExecutionImpl(_ctx, request, response, desktop, page);
			final StringWriter out = new StringWriter(4096*2);
			wappc.getUiEngine().execNewPage(exec, pagedef, page, out);

			//bug 1738368: Jetty refuses wrong content length
			//so we have to calculate it again here
			//(Note: we have to set content length since the servlet
			//being filtering might set content-length, which is,
			//of course, wrong
			String cs = response.getCharacterEncoding();
			if (cs == null || cs.length() == 0) {
				cs = _charset;
				if (cs == null)
					cs = "UTF-8";
			}
			final byte[] bs = out.toString().getBytes(cs);
			response.setContentLength(bs.length);
			response.getOutputStream().write(bs);
		} catch (UiException ex) {
			log.error("Failed to process:\n"+content);
			throw ex;
		} finally {
			I18Ns.cleanup(request, old);
		}
	}
	/** Filters the content to make it legal XML if possible.
	 * Currently, it only removes &lt;!DOCTYPE ...//DTD HTML...&gt;,
	 * since it makes it invalid XML. Refer to Bug 1702216.
	 *
	 * <p>Note: we cannot remove all DOCTYPE. Refer to Bug 1720620.
	 */
	private static String xmlfilter(StringBuffer sb) {
		for (int j = 0, len = sb.length(); j < len; ++j) {
			char cc = sb.charAt(j);
			if (cc == '<') {
				if (++j < len && sb.charAt(j) == '!') {
					j = Strings.skipWhitespaces(sb, j + 1);
					if (j + 7 < len
					&& "DOCTYPE".equalsIgnoreCase(sb.substring(j, j + 7))) {
						for (int k = j += 7; k < len; ++k) {
							if (sb.charAt(k) == '>') {
								if (shallFilter(sb.substring(j, k)))
									return sb.substring(k + 1);
								break;
							}
						}
					}
				}
				break;
			}
		}
		return sb.toString();
	}
	private static boolean shallFilter(String pubId) {
		pubId = pubId.toUpperCase();
		int j = pubId.indexOf("//DTD");
		if (j >= 0) {
			j = Strings.skipWhitespaces(pubId, j + 5);
			return j + 4 < pubId.length()
				&& "HTML".equals(pubId.substring(j, j + 4));
		}
		return false; //unknown => don't filter out (safer)
	}

	//-- Filter --//
	public void doFilter(ServletRequest request, ServletResponse response,
	FilterChain chain) throws IOException, ServletException {
		final StringWriter sw = new StringWriter(4096*2);
		final HttpServletResponse hres = (HttpServletResponse)response;
		final HttpBufferedResponse hbufres =
			(HttpBufferedResponse)HttpBufferedResponse.getInstance(hres, sw);
		chain.doFilter(request, hbufres);

		//Bug 1673839: servlet might redirect
		if (!hbufres.isSendRedirect())
			process((HttpServletRequest)request, hres, xmlfilter(sw.getBuffer()));
	}
	public void destroy() {
	}
	public final void init(FilterConfig config) throws ServletException {
		_ctx = config.getServletContext();

		final String ext = config.getInitParameter("extension");
		if (ext != null && ext.length() > 0)
			_ext = ext;

		final String cs = config.getInitParameter("charset");
		if (cs != null)
			_charset = cs.length() > 0 ? cs: null;
	}
}
