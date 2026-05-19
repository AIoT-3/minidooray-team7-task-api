package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.TagCreateRequest;
import com.nhnacademy.task.dto.req.TagUpdateRequest;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TagController
 *
 * @author chosun-nhn12
 * @since 26. 5. 19.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagResponse> getTags(
            @PathVariable("project-id") Long projectId) {
        return tagService.getTagsByProjectId(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse createTag(
            @PathVariable("project-id") Long projectId,
            @Valid @RequestBody TagCreateRequest request) {
        return tagService.createTag(projectId, request.name());
    }

    @PutMapping("/{tag-id}")
    public TagResponse updateTag(
            @PathVariable("project-id") Long projectId,
            @PathVariable("tag-id") Long tagId,
            @Valid @RequestBody TagUpdateRequest request
    ) {
        return tagService.updateTag(projectId, tagId, request.name());
    }

    @DeleteMapping("/{tag-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(
            @PathVariable("project-id") Long projectId,
            @PathVariable("tag-id") Long tagId
    ) {
        tagService.deleteTag(projectId, tagId);
    }
}
