package idv.chonhan.servlet;

import idv.chonhan.data.CustomerSeat;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class UpdateSeatServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(UpdateSeatServlet.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String id = (req.getParameter("id") != null) ? req.getParameter("id")
				: null;
		String seat = (req.getParameter("seat") != null) ? req
				.getParameter("seat") : null;
		String status = (req.getParameter("status") != null) ? req
				.getParameter("status") : null;

		log.info("id: " + id + ",seat: " + seat + ", status: " + status);

		CustomerSeat cs = new CustomerSeat();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		try {

			Filter idFilter = new FilterPredicate("id", FilterOperator.EQUAL,
					Integer.valueOf(id));

			Query q = new Query("CustomerSeat").setFilter(idFilter);

			PreparedQuery pq = datastore.prepare(q);
			Entity entity = pq.asList(FetchOptions.Builder.withLimit(5)).get(0);

			log.info("entity" + entity.toString());

			if (seat != null) {
				entity.setProperty("seat", seat);
			}
			if (status != null) {
				entity.setProperty("status", status);
			}
			entity.setProperty("updateDate", new Date());
			datastore.put(entity);

			cs.setId(Integer.valueOf(id));
			cs.setName(entity.getProperty("name").toString());
			cs.setSeat(Integer.valueOf(entity.getProperty("seat").toString()));
			cs.setStatus(Integer.valueOf(entity.getProperty("status")
					.toString()));
			cs.setIsVeg(Integer.valueOf(entity.getProperty("isVeg").toString()));
			cs.setIsChild(Integer.valueOf(entity.getProperty("isChild")
					.toString()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setContentType("application/json; charset=UTF-8");

		JSONObject json = new JSONObject(cs);

		resp.getWriter().println(json);
	}
}