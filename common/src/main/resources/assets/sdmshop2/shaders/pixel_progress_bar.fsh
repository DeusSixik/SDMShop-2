#version 150

in vec2 screenPos;
in vec2 guiPos;
in vec2 uv;

out vec4 fragColor;

uniform vec4 SquareVertex;
uniform vec2 ScreenSize;
uniform vec4 TrackColor;
uniform vec4 TrackHighlightColor;
uniform vec4 TrackShadowColor;
uniform vec4 ProgressColor;
uniform vec4 ProgressHighlightColor;
uniform vec4 ProgressShadowColor;
uniform vec4 DividerColor;
uniform float Progress;
uniform float BevelThickness;
uniform float DividerCount;
uniform float DividerThickness;
uniform float DividersEnabled;
uniform float GuiScale;

void main() {
    vec2 localUv = uv * ScreenSize / 2.0;
    localUv.x += ScreenSize.x / 2.0 - SquareVertex.x * GuiScale - ((SquareVertex.z - SquareVertex.x) * GuiScale) / 2.0 - 2.0;
    localUv.y -= ScreenSize.y / 2.0 - SquareVertex.y * GuiScale - ((SquareVertex.w - SquareVertex.y) * GuiScale) / 2.0 - 2.0;

    vec2 extents = vec2((SquareVertex.z - SquareVertex.x) / 2.0, (SquareVertex.w - SquareVertex.y) / 2.0) * GuiScale;
    vec2 size = extents * 2.0;

    float inside = step(abs(localUv.x), extents.x) * step(abs(localUv.y), extents.y);
    if (inside <= 0.0) {
        discard;
    }

    // Same X correction as pixel_bevel_box.fsh: convert mirrored screen quad local
    // space into GUI-left -> GUI-right coordinates.
    vec2 pos = vec2(extents.x - localUv.x, localUv.y + extents.y);
    float bevel = clamp(BevelThickness * GuiScale, 0.0, max(0.0, min(size.x, size.y) * 0.5));
    float left = 1.0 - step(bevel, pos.x);
    float top = 1.0 - step(bevel, pos.y);
    float right = step(size.x - bevel, pos.x);
    float bottom = step(size.y - bevel, pos.y);

    float highlightMask = clamp(max(left, top), 0.0, 1.0);
    float shadowMask = clamp(max(right, bottom), 0.0, 1.0);

    float count = floor(max(0.0, DividerCount));
    if (DividersEnabled > 0.5 && count > 1.0) {
        // HTML analogue:
        // .mc-bar { background: panel; box-shadow: inset bevel; padding: 2px; gap: 2px; }
        // .mc-bar span { background: line-lo; }
        // .mc-bar span.on { background: accent; }
        vec4 color = TrackColor;
        color = mix(color, TrackHighlightColor, highlightMask);
        color = mix(color, TrackShadowColor, shadowMask);

        // Padding is the HTML .mc-bar padding analogue, but it must not eat the
        // whole inner area on very thin bars. If the bar is tiny, keep cells
        // visible and let the bevel render as the outer edge.
        float requestedPadding = max(0.0, BevelThickness * GuiScale);
        float padding = min(requestedPadding, max(0.0, min(size.x, size.y) * 0.25));
        vec2 innerMin = vec2(padding, padding);
        vec2 innerMax = max(innerMin, size - vec2(padding, padding));
        vec2 innerSize = max(vec2(0.0), innerMax - innerMin);
        vec2 innerPos = pos - innerMin;

        float inInner = step(0.0, innerPos.x) * step(0.0, innerPos.y)
                * step(innerPos.x, innerSize.x) * step(innerPos.y, innerSize.y);

        if (inInner > 0.5 && innerSize.x > 0.0 && innerSize.y > 0.0) {
            float requestedGap = max(0.0, DividerThickness * GuiScale);
            float maxGap = count > 1.0 ? max(0.0, (innerSize.x - count) / (count - 1.0)) : 0.0;
            float gap = min(requestedGap, maxGap);
            float cellWidth = max(1.0, (innerSize.x - gap * (count - 1.0)) / count);
            float stride = cellWidth + gap;
            float cellIndex = floor(innerPos.x / stride);
            float cellLocalX = innerPos.x - cellIndex * stride;
            float inCell = step(cellIndex, count - 1.0) * step(cellLocalX, cellWidth);

            if (inCell > 0.5) {
                float filledCells = floor(clamp(Progress, 0.0, 1.0) * count + 0.0001);
                bool filled = cellIndex < filledCells;
                color = filled ? ProgressColor : TrackShadowColor;
            }
        }

        fragColor = color;
        return;
    }

    float progressPx = clamp(Progress, 0.0, 1.0) * size.x;
    bool progressSide = pos.x <= progressPx;
    vec4 base = progressSide ? ProgressColor : TrackShadowColor;
    vec4 high = progressSide ? ProgressHighlightColor : TrackHighlightColor;
    vec4 low = progressSide ? ProgressShadowColor : TrackShadowColor;

    vec4 color = base;
    color = mix(color, high, highlightMask);
    color = mix(color, low, shadowMask);

    fragColor = color;
}
