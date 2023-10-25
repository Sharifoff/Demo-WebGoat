package org.owasp.webgoat;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;

/**

 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class LessonSource extends HammerHead {

    /**
     *
     */
    private static final long serialVersionUID = 2588430536196446145L;

    /**
     * Description of the Field
     */
    public final static String START_SOURCE_SKIP = "START_OMIT_SOURCE";

    public final static String END_SOURCE_SKIP = "END_OMIT_SOURCE";

    /**
     * Description of the Method
     *
     * @param request Description of the Parameter
     * @param response Description of the Parameter
     * @exception IOException Description of the Exception
     * @exception ServletException Description of the Exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String source = null;

        try {
			// System.out.println( "Entering doPost: " );
            // System.out.println( " - request " + request);
            // System.out.println( " - principle: " + request.getUserPrincipal()
            // );
            // setCacheHeaders(response, 0);
            WebSession session = (WebSession) request.getSession(true).getAttribute(WebSession.SESSION);
            // FIXME: Too much in this call.
            session.update(request, response, this.getServletName());

            boolean showSolution = session.getParser().getBooleanParameter("solution", false);
            boolean showSource = session.getParser().getBooleanParameter("source", false);
            if (showSolution) {

                // Get the Java solution of the lesson.
                source = getSolution(session);

                int scr = session.getCurrentScreen();
                Course course = session.getCourse();
                AbstractLesson lesson = course.getLesson(session, scr, AbstractLesson.USER_ROLE);
                lesson.getLessonTracker(session).setViewedSolution(true);

            } else if (showSource) {

                // Get the Java source of the lesson. FIXME: Not needed
                source = getSource(session);

                int scr = session.getCurrentScreen();
                Course course = session.getCourse();
                AbstractLesson lesson = course.getLesson(session, scr, AbstractLesson.USER_ROLE);
                lesson.getLessonTracker(session).setViewedSource(true);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log("ERROR: " + t);
        } finally {
            try {
                this.writeSource(source, response);
            } catch (Throwable thr) {
                thr.printStackTrace();
                log(request, "Could not write error screen: " + thr.getMessage());
            }
            // System.out.println( "Leaving doPost: " );

        }
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected String getSource(WebSession s) {

        String source = null;
        int scr = s.getCurrentScreen();
        Course course = s.getCourse();

        if (s.isUser() || s.isChallenge()) {

            AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);

            if (lesson != null) {
                source = lesson.getSource(s);
            }
        }
        if (source == null) {
            return "Source code is not available. Contact "
                    + s.getWebgoatContext().getFeedbackAddressHTML();
        }
        return (source.replaceAll("(?s)" + START_SOURCE_SKIP + ".*" + END_SOURCE_SKIP,
                "Code Section Deliberately Omitted"));
    }

    protected String getSolution(WebSession s) {

        String source = null;
        int scr = s.getCurrentScreen();
        Course course = s.getCourse();

        if (s.isUser() || s.isChallenge()) {

            AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);

            if (lesson != null) {
                source = lesson.getSolution(s);
            }
        }
        if (source == null) {
            return "Solution  is not available. Contact "
                    + s.getWebgoatContext().getFeedbackAddressHTML();
        }
        return (source);
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @param response Description of the Parameter
     * @exception IOException Description of the Exception
     */
    protected void writeSource(String s, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        if (s == null) {
            s = new String();
        }

        out.print(s);
        out.close();
    }
}
