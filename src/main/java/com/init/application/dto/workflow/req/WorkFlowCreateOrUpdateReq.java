package com.init.application.dto.workflow.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(title = "워크플로우 생성 및 수정 요청 DTO")
public record WorkFlowCreateOrUpdateReq(
        @Schema(description = "워크플로우 제목", example = "드론 동작 워크플로우")
        @NotEmpty
        String title,
        @Schema(
                title = "워크플로우 노드 JSON 정보",
                example = """
                        {
                          "version": 1,
                          "nodes": [
                            {
                              "id": "1770601285301-d5d32f4d4c2b1",
                              "type": "textNode",
                              "position": {
                                "x": 370.8040544340595,
                                "y": 69.53942250237196
                              },
                              "data": {
                                "title": "새 노드",
                                "content": "",
                                "attachments": [],
                                "updatedAt": 1770601285301
                              },
                              "measured": {
                                "width": 260,
                                "height": 50
                              },
                              "selected": false,
                              "dragging": false
                            },
                            {
                              "id": "1770279921531-11b05f3a7ca15",
                              "type": "textNode",
                              "position": {
                                "x": 88.5,
                                "y": 138.5
                              },
                              "data": {
                                "title": "제목",
                                "content": "내용",
                                "attachments": [],
                                "updatedAt": 1770601293862
                              },
                              "measured": {
                                "width": 260,
                                "height": 81
                              },
                              "selected": true,
                              "dragging": false
                            }
                          ],
                          "edges": [
                            {
                              "type": "smoothstep",
                              "style": {
                                "stroke": "rgba(255,255,255,0.55)",
                                "strokeWidth": 2
                              },
                              "markerEnd": {
                                "type": "arrowclosed"
                              },
                              "source": "1770279921531-11b05f3a7ca15",
                              "target": "1770601285301-d5d32f4d4c2b1",
                              "label": "",
                              "id": "xy-edge__1770279921531-11b05f3a7ca15-1770601285301-d5d32f4d4c2b1"
                            }
                          ],
                          "viewport": {
                            "x": 128.06566231180727,
                            "y": 242.1431574476045,
                            "zoom": 1.1953356399956647
                          },
                          "updatedAt": 1770601327901
                        }
                        """
        )
        @NotNull
        Object nodeInfo
) {
}
