<!DOCTYPE HTML>

<%@ page import="ua.edu.nau.helper.constant.Attribute" %>
<%@ page import="ua.edu.nau.helper.constant.Parameter" %>
<%@ page import="ua.edu.nau.model.Test" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ua.edu.nau.helper.TimeFormatter" %>
<%@ page import="ua.edu.nau.helper.constant.RoleCode" %>
<%@ page import="ua.edu.nau.model.User" %>
<%@ page import="ua.edu.nau.helper.constant.JSPEvent" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = ((User) request.getAttribute(Attribute.ATTR_USER_MODEL));
    ArrayList<Test> tests = ((ArrayList<Test>) request.getAttribute(Attribute.ATTR_ARRAY_LIST_TEST));
%>

<html>
<head>
    <title>Мої тести</title>

    <jsp:include page="/jsp/mdl_commons.jsp"/>

    <link href="${pageContext.request.contextPath}/resources/css/drawer_style.css" rel="stylesheet">
</head>
<body onload="checkEvents()">

<style>
    body {
        width: 100%;
        max-width: 100%;
        background-color: #f9f9f9;
    }

    .page-content {
        width: 100%;
        padding-bottom: 24px;
    }

    .card-square {
        width: 100%;
        max-width: 100%;
        height: 100%;
        margin-top: 16px;
    }

    .mdl-card__title {
        height: 100px;
        padding-left: 16px;
        padding-bottom: 8px;
        color: #ffffff;
        background-color: #2196F3;
    }

    @media screen
    and (max-device-width: 1000px)
    and (max-device-height: 1200px) {
        .page-content {
            width: 100%;
            min-width: 100%;
            margin: 0;
            padding-bottom: 24px;
        }

        .mdl-grid {
            width: 95%;
            min-width: 95%;
        }

        .card-square {
            width: 100%;
            max-width: 100%;
            margin-top: 16px;
        }
    }

    .mdl-button--fab {
        position: fixed;
        right: 36px;
        bottom: 36px;
        z-index: 999;
    }

    .menu-lower-right {
        position: absolute;
        top: 0;
        right: 0;
        margin: 8px;
    }
</style>

<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title">Мої тести</span>
            <div class="mdl-layout-spacer"></div>
            <nav class="mdl-navigation mdl-layout--large-screen-only">
            </nav>
        </div>
    </header>

    <%
        if (user.getUserRole().getRoleCode().equals(RoleCode.STUDENT)) {
    %>
    <jsp:include page="/jsp/drawer_student.jsp"/>
    <%
    } else if (user.getUserRole().getRoleCode().equals(RoleCode.ROOT)) {
    %>
    <jsp:include page="/jsp/drawer_root.jsp"/>
    <%
        }
    %>

    <main id="mdl-layout__content" class="mdl-layout__content">
        <script type="text/javascript">
            function deleteTest(formId) {
                document.getElementById(formId).submit();
            }

            function editTest(formId) {
                document.getElementById(formId).submit();
            }

            function checkEvents() {
//                showSnackBar("LOAD");

                <%
                if(request.getAttribute(Attribute.ATTR_JSP_EVENT) != null){
                    String event =((String) request.getAttribute(Attribute.ATTR_JSP_EVENT));

                    if(event.equals(JSPEvent.TEST_DELETED)){
                    %>
//                showSnackBar("Тест видалено");
                <%
                }
            }
           %>
            }
        </script>

        <div class="page-content">
            <div class="mdl-grid">
                <%
                    for (Test test : tests) {
                %>
                <div class="mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet">
                    <div class="card-square mdl-card mdl-shadow--2dp">
                        <div class="mdl-card__title mdl-card--expand">
                            <h2 class="mdl-card__title-text">
                                <%=test.getName()%>
                            </h2>

                            <!-- Right aligned menu below button -->
                            <button id="menu-lower-right-<%=test.getId()%>"
                                    type="button"
                                    class="mdl-button mdl-js-button mdl-button--icon menu-lower-right">
                                <i class="material-icons">more_vert</i>
                            </button>

                            <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect"
                                for="menu-lower-right-<%=test.getId()%>">
                                <form id="form-delete-test-<%=test.getId()%>"
                                      action="${pageContext.request.contextPath}/me/tests" method="post">
                                    <input type="hidden" name="<%=Parameter.PARAM_TEST_ID%>" value="<%=test.getId()%>">
                                    <input type="hidden" name="<%=Parameter.PARAM_ACTION%>"
                                           value="<%=Parameter.PARAM_ACTION_DELETE_TEST%>">
                                    <li class="mdl-menu__item"
                                        onclick="deleteTest('form-delete-test-<%=test.getId()%>')">
                                        Видалити
                                    </li>
                                </form>

                                <%--<form id="form-update-test-<%=test.getId()%>"
                                      action="${pageContext.request.contextPath}/tests/edit" method="get">
                                    <input type="hidden" name="<%=Parameter.PARAM_TEST_ID%>" value="<%=test.getId()%>">
                                    <li class="mdl-menu__item"
                                        onclick="editTest('form-update-test-<%=test.getId()%>')">
                                        Редагувати
                                    </li>
                                </form>--%>
                            </ul>
                        </div>

                        <div class="mdl-card__supporting-text">
                            <%=test.getDescription()%>
                            <br/>
                            Час виконання
                            <%=TimeFormatter.dateToHumanMinutes(test.getTime())%>
                        </div>

                        <div class="mdl-card__actions mdl-card--border">
                            <form action="${pageContext.request.contextPath}/test/info" method="post">
                                <input type="hidden" name="<%=Parameter.PARAM_TEST_ID%>" value="<%=test.getId()%>">
                                <button class="mdl-button mdl-js-button mdl-button--primary"
                                        style="float: left;"
                                        type="submit">
                                    Детальніше
                                </button>
                            </form>

                            <form action="${pageContext.request.contextPath}/tests/sessions" method="post">
                                <input type="hidden" name="<%=Parameter.PARAM_TEST_ID%>" value="<%=test.getId()%>">
                                <button class="mdl-button mdl-js-button mdl-button--primary"
                                        type="submit">
                                    Розпочати
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/tests/add">
            <button class="mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--colored"
                    id="fab-test-add">
                <i class="material-icons">add</i>
            </button>
        </a>
    </main>

    <div class="mdl-tooltip mdl-tooltip--left" data-mdl-for="fab-test-add">
        Додати тест
    </div>
</div>
</body>
</html>
