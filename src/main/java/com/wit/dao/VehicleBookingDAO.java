package com.wit.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wit.dto.EmployeeDTO;
import com.wit.dto.VehicleBookingDTO;
import com.wit.dto.VehiclesDTO;

@Repository
public class VehicleBookingDAO {
	
	@Autowired
	private SqlSession mybatis;
	
	// 차량별 예약 목록
	public List<VehiclesDTO> getVehicleList() {
		return mybatis.selectList("vehicleBooking.getVehicleList");
	}
	
	// 해당 차량 정보 받아오기
	public VehiclesDTO getVehicleInfo(int vehicleSeq) {
		return mybatis.selectOne("vehicleBooking.getVehicleInfo", vehicleSeq);
	}
	
	// 차량 예약 추가
	public int saveVehicle(VehicleBookingDTO dto) {
		return mybatis.insert("vehicleBooking.vehicleBookingInsert", dto);
	}
	
	// 차량 예약 목록 조회
	public List<VehicleBookingDTO> getAllVehicleBooking(String vehicleSeq){
		return mybatis.selectList("vehicleBooking.getAllVehicleBooking", Integer.parseInt(vehicleSeq));
	}
	
	// 직원 정보 조회 메소드 추가
	public EmployeeDTO employeeInfo(String empNo) {
		return mybatis.selectOne("vehicleBooking.employeeInfo", empNo);
	}
}
