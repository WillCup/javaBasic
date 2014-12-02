package com.will.tooljars.apache.tomcat.valve;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

/**
 * A Tomcat valve that introduces processing delay into random requests
 * 
 * The attributes are:
 * 
 * pctDelayedRequests - the percentage of requests that will have a delay added
 * maxDelay - the maximum amount of time (in msec) that will be added minDelay -
 * the minimum amount of time (in msec) that will be added
 * 
 */
public class RandomDelayValve extends ValveBase {

    /** The percentage of requests that will have a delay added. */
    private int percentDelayed = 0;

    /** The maximum amount of delay to add (in milliseconds) */
    private int maxDelay = 10000;

    /** The minimum amount of delay to add (in milliseconds) */
    private int minDelay = 5000;

    private static final int DEFAULT_DELAY_SCALE_FACTOR = 2;
    private static Random random = new Random();
    private boolean maxDelaySet = false;
    private boolean minDelaySet = false;

    /**
     * If selected as a "slow" request (via random number and percentage
     * parameter), add a certain amount of delay to simulate a long-running
     * request.
     */
    public void invoke(Request request, Response response) throws IOException,
            ServletException {
        /*
         * Use percentage to find out if this request is delayed.
         */
//        boolean willBeDelayed = (random.nextInt(100) > percentDelayed);
//
//        if (willBeDelayed) {
//            int delay;
//
//            delay = random.nextInt(maxDelay - minDelay) + minDelay;
//
//            /*
//             * Report delay
//             */
//            if (containerLog.isInfoEnabled()) {
//                containerLog.info("Delaying request "
//                        + request.getDecodedRequestURI() + " for " + delay
//                        + " ms.");
//            }
//
//            /*
//             * Delay request by sleeping
//             */
//            try {
//                Thread.sleep(delay);
//            } catch (InterruptedException ie) {
//                containerLog.error("Got an exception while sleeping.");
//            }
//        }

//        response.recycle();
//        response.resetBuffer(true);
        ServletOutputStream out = null;
        if (!request.getRequestURL().toString().endsWith(".gif")) {
            try {
                out = response.getOutputStream();
                
                out.println("<html>");
                out.println("<head>");
                out.println("<title>SimpleServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p>Hello, World!</p>");
                out.println("<p>Hello, this is my first servlet!</p>");
                out.println("</body>");
                out.println("</html>");
            } catch (Exception e) {
                containerLog.info(" in exception");
                e.printStackTrace();
            } 
        }

        /*
         * Invoke next valve or true processing
         */
        getNext().invoke(request, response);
        containerLog.info("host name is : " + request.getHost().getName());
        
        
//        PrintWriter out  = response.getWriter();
//
//        out.println("<html>");
//        out.println("<head>");
//        out.println("<title>SimpleServlet</title>");
//        out.println("</head>");
//        out.println("<body>");
//        out.println("<p>Hello, World!</p>");
//        out.println("<p>Hello, this is my first servlet!</p>");
//        out.println("</body>");
//        out.println("</html>");
        containerLog.info("host info is : " + request.getHost().getInfo());
        containerLog.info("this.container.info is : " + this.getContainer().getInfo());
        Valve[] valves1 = this.getContainer().getPipeline().getValves();
        for (Valve v : valves1) {
            containerLog.info(v.getClass().getName() + " -->> " + v.getInfo());
        }
        containerLog.info("request is : " + request);
        if (request.getWrapper() != null) {
            containerLog.info("requestWrapper is : " + request.getWrapper());
            containerLog.info("request.getWrapper().getPipeline() is :" + request.getWrapper().getPipeline());
            Valve[] valves = request.getWrapper().getPipeline().getValves();
            for (Valve v : valves) {
                containerLog.info(v.getClass().getName() + " --> " + v.getInfo());
            }
        }

        containerLog
                .info("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        containerLog.info("Request is " + response.getRequest());
        containerLog.info("Response status is " + response.getStatus());
        response.addHeader("willtest", "Will Chen's Test");
        containerLog
                .info("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    }

    /**
     * Set percentDelayed
     */
    public void setPercentDelayed(int pctDelay) throws OutOfRangeException {
        if (pctDelay < 0) {
            throw new OutOfRangeException("PercentDelayed must be positive");
        } else if (pctDelay > 100) {
            throw new OutOfRangeException("PercentDelayed must be <= 100");
        }
        percentDelayed = pctDelay;
    }

    /**
     * Set maxDelay
     */
    public void setMaxDelay(int delay) throws OutOfRangeException {
        if (delay < 0) {
            throw new OutOfRangeException("maxDelay must be positive.");
        }
        if (minDelaySet) {
            if (delay < minDelay) {
                throw new OutOfRangeException("maxDelay must be >= minDelay");
            }
        } else {
            minDelay = delay / DEFAULT_DELAY_SCALE_FACTOR;
        }
        maxDelay = delay;
        maxDelaySet = true;
    }

    /**
     * Set minDelay
     */
    public void setMinDelay(int delay) throws OutOfRangeException {
        if (delay < 0) {
            throw new OutOfRangeException("minDelay must be positive.");
        }
        if (maxDelaySet) {
            if (delay > maxDelay) {
                throw new OutOfRangeException("minDelay must be <= maxDelay");
            }
        } else {
            maxDelay = delay * DEFAULT_DELAY_SCALE_FACTOR;
        }
        minDelay = delay;
        minDelaySet = true;
    }

    /**
     * @see org.apache.catalina.valves.ValveBase#getInfo()
     */
    public String getInfo() {
        return getClass() + "/1.0";
    }
}