package atndbot.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent 
    private String title;

    @Persistent 
    private String titleCatch;

    @Persistent 
    private Text description;

    @Persistent 
    private Text event_url;

    @Persistent 
    private Date started_at;

    @Persistent 
    private int limit;
    
    @Persistent 
    private String address;
    
    @Persistent 
    private String place;
    
    @Persistent 
    private GeoPt lat_lon;
    
    @Persistent 
    private int accepted;
    
    @Persistent 
    private int waiting;
    
    @Persistent 
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
}
