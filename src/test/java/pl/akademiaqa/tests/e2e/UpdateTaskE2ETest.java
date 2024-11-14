package pl.akademiaqa.tests.e2e;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.akademiaqa.dto.requests.CreateTaskRequestDto;
import pl.akademiaqa.requests.list.CreateListRequest;
import pl.akademiaqa.requests.space.CreateSpaceRequest;
import pl.akademiaqa.requests.space.DeleteSpaceRequest;
import pl.akademiaqa.requests.task.CreateTaskRequest;
import pl.akademiaqa.requests.task.UpdateTaskRequest;

class UpdateTaskE2ETest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTaskE2ETest.class);
    private static String spaceName = "SpaceE2E";
    private static String listName = "Zadania";
    private static String taskName = "Przetestować ClickUp";
    private String spaceId;
    private String listId;
    private String tasktId;

    @Test
    void updateTaskE2ETest() {
        spaceId = createSpaceStep();
        LOGGER.info("Space created with id {}", spaceId);


        listId = createListStep();
        LOGGER.info("List created with id {}", listId);


        tasktId = createTaskStep();
        LOGGER.info("Task created with id {}", tasktId);

        updateTaskStep();
        closeTaskStep();
        deleteSpace();

    }

    private String createSpaceStep() {
        JSONObject json = new JSONObject();
        json.put("name", spaceName);

        final Response response = CreateSpaceRequest.createSpace(json);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonData = response.jsonPath();
        Assertions.assertThat(jsonData.getString("name")).isEqualTo(spaceName);

        return jsonData.getString("id");
    }

    private String createListStep() {
        JSONObject json = new JSONObject();
        json.put("name", listName);

        final Response listResponse = CreateListRequest.createList(json, spaceId);
        Assertions.assertThat(listResponse.statusCode()).isEqualTo(200);

        JsonPath jsonData = listResponse.jsonPath();
        Assertions.assertThat(jsonData.getString("name")).isEqualTo(listName);

        return jsonData.getString("id");
    }

    private String createTaskStep() {

        CreateTaskRequestDto taskDto = new CreateTaskRequestDto();
        taskDto.setName(taskName);
        taskDto.setDescription("Ciekawe jak to działa");
        taskDto.setStatus("to do");
        taskDto.setPriority("3");

        final var response = CreateTaskRequest.createTask(taskDto, listId);

        Assertions.assertThat(response.getName()).isEqualTo(taskName);
        Assertions.assertThat(response.getDescription()).isEqualTo("Ciekawe jak to działa");

        return response.getId();
    }

    private void updateTaskStep() {

        JSONObject updateTask = new JSONObject();
        updateTask.put("name", "Zmieniona nazwa");
        updateTask.put("description", "Zmieniony opis zadania");
        updateTask.put("assignees", "");
        updateTask.put("status", "to do");
        updateTask.put("priority", "3");

        final var response = UpdateTaskRequest.updateTask(updateTask, tasktId);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonData = response.jsonPath();
        Assertions.assertThat(jsonData.getString("name")).isEqualTo("Zmieniona nazwa");
        Assertions.assertThat(jsonData.getString("description")).isEqualTo("Zmieniony opis zadania");

    }

    private void closeTaskStep() {

        JSONObject closeTask = new JSONObject();
        closeTask.put("status", "complete");

        Response response = UpdateTaskRequest.updateTask(closeTask, tasktId);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonData = response.jsonPath();
        Assertions.assertThat(jsonData.getString("status.status")).isEqualTo("complete");
    }

    private void deleteSpace() {

        Response response = DeleteSpaceRequest.deleteSpace(spaceId);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
    }
}



