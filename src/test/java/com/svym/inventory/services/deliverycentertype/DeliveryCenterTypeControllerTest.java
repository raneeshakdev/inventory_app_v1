package com.svym.inventory.services.deliverycentertype;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svym.inventory.service.deliverycentertype.DeliveryCenterTypeController;
import com.svym.inventory.service.deliverycentertype.DeliveryCenterTypeService;
import com.svym.inventory.service.dto.DeliveryCenterTypeDTO;

class DeliveryCenterTypeControllerTest {

	private DeliveryCenterTypeService service;
	private DeliveryCenterTypeController controller;
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		service = mock(DeliveryCenterTypeService.class);
		controller = new DeliveryCenterTypeController(service);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void testGetAllDeliveryCenterTypes() throws Exception {
		DeliveryCenterTypeDTO dto1 = new DeliveryCenterTypeDTO(1L, "Type1", null);
		DeliveryCenterTypeDTO dto2 = new DeliveryCenterTypeDTO(2L, "Type2", null);

		when(service.getAll()).thenReturn(Arrays.asList(dto1, dto2));

		mockMvc.perform(get("/api/delivery-center-types")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2));

		verify(service, times(1)).getAll();
	}

	@Test
	void testGetDeliveryCenterTypeById() throws Exception {
		DeliveryCenterTypeDTO dto = new DeliveryCenterTypeDTO(1L, "Type1", null);

		when(service.getById(1L)).thenReturn(dto);

		mockMvc.perform(get("/api/delivery-center-types/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L));

		verify(service).getById(1L);
	}

	@Test
	void testCreateDeliveryCenterType() throws Exception {
		DeliveryCenterTypeDTO inputDto = new DeliveryCenterTypeDTO(null, "NewType", null);
		DeliveryCenterTypeDTO savedDto = new DeliveryCenterTypeDTO(3L, "NewType", null);

		when(service.create(any(DeliveryCenterTypeDTO.class))).thenReturn(savedDto);

		mockMvc.perform(post("/api/delivery-center-types").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(3L));
	}

	@Test
	void testUpdateDeliveryCenterType() throws Exception {
		DeliveryCenterTypeDTO inputDto = new DeliveryCenterTypeDTO(1L, "UpdatedType", null);

		when(service.update(eq(1L), any(DeliveryCenterTypeDTO.class))).thenReturn(inputDto);

		mockMvc.perform(put("/api/delivery-center-types/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)) // optional but good for coverage
				.andExpect(jsonPath("$.typeName").value("UpdatedType"));
	}

	@Test
	void testDeleteDeliveryCenterType() throws Exception {
		doNothing().when(service).delete(1L);

		mockMvc.perform(delete("/api/delivery-center-types/1")).andExpect(status().isNoContent());

		verify(service).delete(1L);
	}
}
