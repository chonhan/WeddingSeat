package idv.chonhan.servlet;

import idv.chonhan.data.CustomerSeat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class WeddingSeatServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		List<Entity> csList = new ArrayList<Entity>();

		try {
			Query q = new Query("CustomerSeat").addSort("name",
					SortDirection.ASCENDING);

			PreparedQuery pq = datastore.prepare(q);
			csList = pq.asList(FetchOptions.Builder.withLimit(400));

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setContentType("application/json; charset=UTF-8");

		JSONArray jarray = new JSONArray();
		for (Entity entity : csList) {

			CustomerSeat cs = new CustomerSeat();

			cs.setId(Integer.valueOf(entity.getProperty("id").toString()));
			cs.setName(entity.getProperty("name").toString());
			cs.setSeat(Integer.valueOf(entity.getProperty("seat").toString()));
			cs.setStatus(Integer.valueOf(entity.getProperty("status")
					.toString()));
			cs.setIsVeg(Integer.valueOf(entity.getProperty("isVeg").toString()));
			cs.setIsChild(Integer.valueOf(entity.getProperty("isChild")
					.toString()));

			JSONObject jobj = new JSONObject(cs);
			jarray.put(jobj);
		}

		resp.getWriter().println(jarray);
	}
}
