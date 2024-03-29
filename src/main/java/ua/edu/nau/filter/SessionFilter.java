package ua.edu.nau.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.edu.nau.dao.HttpSessionDAO;
import ua.edu.nau.dao.UserDAO;
import ua.edu.nau.helper.SessionHelper;
import ua.edu.nau.helper.constant.RoleCode;
import ua.edu.nau.helper.session.SessionUtils;
import ua.edu.nau.model.HttpSession;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "sessionFilter")
public class SessionFilter implements Filter {
    private UserDAO userDAO;
    private HttpSessionDAO httpSessionDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setHttpSessionDAO(HttpSessionDAO httpSessionDAO) {
        this.httpSessionDAO = httpSessionDAO;
    }

    private static final String TAG = "SessionFilter -> ";

    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service.
     * <p>
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     * <p>
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws a ServletException
     * <li>Does not return within a time period defined by the web container
     * </ol>
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time a request/response pair is passed through the
     * chain due to a client request for a resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     * <p>
     * <p>A typical implementation of this method would follow the following
     * pattern:
     * <ol>
     * <li>Examine the request
     * <li>Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering
     * <li>Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering
     * <li>
     * <ul>
     * <li><strong>Either</strong> invoke the next entity in the chain
     * using the FilterChain object
     * (<code>chain.doFilter()</code>),
     * <li><strong>or</strong> not pass on the request/response pair to
     * the next entity in the filter chain to
     * block the request processing
     * </ul>
     * <li>Directly set headers on the response after invocation of the
     * next entity in the filter chain.
     * </ol>
     *
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Инициализация вспомагательного класса
        SessionUtils sessionUtils = new SessionUtils(((HttpServletRequest) request).getSession());

        // Проверка сессии на существование
        if (sessionUtils.getHttpSessionId() != null) {
            // Инициализация классов для доступа к БД

            // Получение последней сессии пользвателя
            HttpSession lastHttpSession = userDAO.getLastSession(sessionUtils.getUser().getId());

            // Проверка сессии на существование
            if (lastHttpSession == null) {
                // Пользователь впервые зашел
                System.out.println(TAG + "Can't fetch last http session");
                chain.doFilter(request, response);
                return;
            }

            // Проверка роли пользователя
            if (sessionUtils.getUser().getUserRole().getRoleCode().equals(RoleCode.STUDENT)) {
                // Если время сессии вышло - инвалидируем ее
                if (SessionHelper.isSessionTimedOut(lastHttpSession)) {
                    System.out.println(TAG + "Session timed out");

                    // Инвалидация сессии в БД
                    httpSessionDAO.invalidate(sessionUtils.getHttpSessionId());
                    // Рандомизация пароля
                    userDAO.randomizePassword(sessionUtils.getUser().getId());

                    // Инвалидация JavaEE сессии
                    ((HttpServletRequest) request).getSession().invalidate();
                    ((HttpServletResponse) response).sendRedirect("/login");

                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     * <p>
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after a timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     * <p>
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {

    }
}
