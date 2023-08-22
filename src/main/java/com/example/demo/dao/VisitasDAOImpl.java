package com.example.demo.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Visitas;
import com.example.demo.model.VisitasRowMapper;

@Repository
public class VisitasDAOImpl implements VisitasDAO{
	private final JdbcTemplate jdbcTemplate;

	public VisitasDAOImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Visitas> ListarVisitas() {
        String sql = "SELECT id, cliente_id, fecha_visita, detalle, profesional_id FROM visitas;";
		return jdbcTemplate.query(sql, new VisitasRowMapper());
	}
}
