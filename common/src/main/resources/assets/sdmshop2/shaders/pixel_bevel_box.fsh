#version 150

in vec2 screenPos;
in vec2 guiPos;
in vec2 uv;

out vec4 fragColor;

uniform vec4 SquareVertex;
uniform vec2 ScreenSize;
uniform vec4 FillColor;
uniform vec4 HighlightColor;
uniform vec4 ShadowColor;
uniform float BevelThickness;
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

    // Screen quad UV is mirrored on X relative to GUI coordinates here, so convert
    // local space back to GUI-left -> GUI-right before calculating bevel sides.
    vec2 pos = vec2(extents.x - localUv.x, localUv.y + extents.y);
    float bevel = clamp(BevelThickness * GuiScale, 0.0, max(0.0, min(size.x, size.y) * 0.5));

    float left = 1.0 - step(bevel, pos.x);
    float top = 1.0 - step(bevel, pos.y);
    float right = step(size.x - bevel, pos.x);
    float bottom = step(size.y - bevel, pos.y);

    float highlightMask = clamp(max(left, top), 0.0, 1.0);
    float shadowMask = clamp(max(right, bottom), 0.0, 1.0);

    vec4 color = FillColor;
    color = mix(color, HighlightColor, highlightMask);
    color = mix(color, ShadowColor, shadowMask);

    fragColor = color;
}
