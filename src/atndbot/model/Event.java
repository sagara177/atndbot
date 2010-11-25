package atndbot.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private String title;

    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private String titleCatch;

    @Persistent 
    private Text description;

    @Persistent 
    private Text event_url;

    @Persistent 
    private Date started_at;

    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private Date ended_at;

    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private int limit;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private String address;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private String place;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private GeoPt lat_lon;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private int accepted;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private int waiting;
    
    @Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value="true")
    private Date updated_at;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleCatch() {
		return titleCatch;
	}

	public void setTitleCatch(String titleCatch) {
		this.titleCatch = titleCatch;
	}

	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public Text getEvent_url() {
		return event_url;
	}

	public void setEvent_url(Text eventUrl) {
		event_url = eventUrl;
	}

	public Date getStarted_at() {
		return started_at;
	}

	public void setStarted_at(Date startedAt) {
		started_at = startedAt;
	}

	public Date getEnded_at() {
		return ended_at;
	}

	public void setEnded_at(Date endedAt) {
		ended_at = endedAt;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public GeoPt getLat_lon() {
		return lat_lon;
	}

	public void setLat_lon(GeoPt latLon) {
		lat_lon = latLon;
	}

	public int getAccepted() {
		return accepted;
	}

	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

	public int getWaiting() {
		return waiting;
	}

	public void setWaiting(int waiting) {
		this.waiting = waiting;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updatedAt) {
		updated_at = updatedAt;
	}

	@Override
	public String toString() {
		return title + ", " + started_at + ", " + event_url;
	}
}
