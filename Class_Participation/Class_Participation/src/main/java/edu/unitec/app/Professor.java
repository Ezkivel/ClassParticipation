package edu.unitec.app;

/**
 * Created by Henry on 11-29-13.
 */

public class Professor {

    private int _ProfessorId;
	private String _ProfessorName;
	private String _ProfessorEmail;

    public Professor(){}

    public Professor(int id, String name, String email){
		_ProfessorId = id;
		_ProfessorName = name;
		_ProfessorEmail = email;

	}
	public int get_ProfessorId() {
		return _ProfessorId;
	}
	public void set_ProfessorId(int _ProfessorId) {
		this._ProfessorId = _ProfessorId;
	}
	public String get_ProfessorName() {
		return _ProfessorName;
	}
	public void set_ProfessorName(String _ProfessorName) {
		this._ProfessorName = _ProfessorName;
	}
	public String get_ProfessorEmail() {
		return _ProfessorEmail;
	}
	public void set_ProfessorEmail(String _ProfessorEmail) {
		this._ProfessorEmail = _ProfessorEmail;
	}
	
}