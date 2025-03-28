# Design System Documentation

## Color System

### Primary Colors
- Primary Blue: `#1976D2` (Material Blue 700)
- Primary Light: `#64B5F6` (Material Blue 300)
- Primary Dark: `#0D47A1` (Material Blue 900)

### Surface Colors
- Background: `#FFFFFF` (White)
- Surface: `#F5F5F5` (Grey 100)
- Card Background: `#FFFFFF` (White)
- Divider: `#E0E0E0` (Grey 300)

### Text Colors
- Primary Text: `#212121` (Grey 900)
- Secondary Text: `#757575` (Grey 600)
- Hint Text: `#9E9E9E` (Grey 500)

### Status Colors
- Success: `#4CAF50` (Green 500)
- Error: `#F44336` (Red 500)
- Warning: `#FFC107` (Amber 500)
- Info: `#2196F3` (Blue 500)

## Typography

### Font Family
- Primary: Roboto
- Fallback: sans-serif

### Text Styles
- Headline Large: 32sp, Bold
- Headline Medium: 24sp, Bold
- Headline Small: 20sp, Bold
- Title Large: 22sp, Medium
- Title Medium: 16sp, Medium
- Title Small: 14sp, Medium
- Body Large: 16sp, Regular
- Body Medium: 14sp, Regular
- Body Small: 12sp, Regular
- Label Large: 14sp, Medium
- Label Medium: 12sp, Medium
- Label Small: 11sp, Medium

## Spacing System

### Base Unit: 8dp
- 4dp: Extra small spacing
- 8dp: Small spacing
- 16dp: Medium spacing
- 24dp: Large spacing
- 32dp: Extra large spacing

### Component Spacing
- Screen Padding: 16dp
- Card Padding: 16dp
- List Item Spacing: 8dp
- Section Spacing: 24dp

## Component Specifications

### Buttons
1. **Primary Button**
   - Height: 48dp
   - Padding: 24dp horizontal
   - Background: Primary Blue
   - Text Color: White
   - Corner Radius: 4dp
   - Elevation: 2dp

2. **Secondary Button**
   - Height: 48dp
   - Padding: 24dp horizontal
   - Background: Transparent
   - Border: 1dp, Primary Blue
   - Text Color: Primary Blue
   - Corner Radius: 4dp

3. **Text Button**
   - Height: 48dp
   - Padding: 12dp horizontal
   - Background: Transparent
   - Text Color: Primary Blue
   - No elevation

### Cards
1. **Flight Card**
   - Elevation: 2dp
   - Corner Radius: 8dp
   - Padding: 16dp
   - Content Layout:
     - Airline Logo: 40dp x 40dp
     - Flight Info: 16dp spacing
     - Price: Right-aligned
   - Minimum Height: 120dp

2. **Search Card**
   - Elevation: 1dp
   - Corner Radius: 8dp
   - Padding: 16dp
   - Content Layout:
     - Input Fields: 16dp spacing
     - Search Button: Full width

### Input Fields
1. **Text Input**
   - Height: 56dp
   - Padding: 16dp horizontal
   - Background: Surface
   - Border: 1dp, Grey 300
   - Corner Radius: 4dp
   - Label: 12sp, Grey 600
   - Text: 16sp, Grey 900

2. **Date Picker**
   - Height: 56dp
   - Padding: 16dp horizontal
   - Background: Surface
   - Border: 1dp, Grey 300
   - Corner Radius: 4dp
   - Icon: 24dp x 24dp

### Loading States
1. **Loading Circle**
   - Size: 48dp x 48dp
   - Color: Primary Blue
   - Stroke Width: 4dp
   - Animation Duration: 1s

2. **Skeleton Loading**
   - Background: Grey 200
   - Animation: Shimmer effect
   - Duration: 1.5s

### Offline Status Bar
- Height: 40dp
- Background: Warning color
- Text: White, 14sp
- Icon: 20dp x 20dp
- Padding: 8dp horizontal

## Layout Guidelines

### Screen Layout
- Safe Area: Respect system bars
- Content Padding: 16dp
- Bottom Navigation Height: 56dp

### List Layout
- Item Spacing: 8dp
- Divider: 1dp, Grey 200
- Padding: 16dp horizontal

### Grid Layout
- Column Count: 2
- Spacing: 8dp
- Item Aspect Ratio: 1:1

## Animation Specifications

### Duration
- Quick: 150ms
- Normal: 250ms
- Slow: 350ms

### Easing
- Standard: FastOutSlowIn
- Decelerate: DecelerateEasing
- Accelerate: AccelerateEasing

### Transitions
- Screen Transition: Fade (250ms)
- List Item: Slide (200ms)
- Loading: Rotate (1000ms)

## Best Practices

1. **Component Usage**
   - Use predefined components
   - Maintain consistent spacing
   - Follow Material Design guidelines
   - Keep animations minimal

2. **Layout**
   - Use ConstraintLayout for complex layouts
   - Implement responsive design
   - Handle different screen sizes
   - Support basic orientation changes

3. **Performance**
   - Use lazy loading for lists
   - Implement view recycling
   - Minimize layout nesting
   - Cache images and data

4. **State Management**
   - Handle loading states
   - Show error messages
   - Display empty states
   - Indicate offline status

## Implementation Notes

1. **Jetpack Compose**
   - Use Material3 components
   - Implement custom components
   - Follow Compose best practices
   - Use remember and mutableStateOf

2. **Testing**
   - Test different screen sizes
   - Verify offline behavior
   - Check state transitions
   - Validate user interactions

3. **Documentation**
   - Document component usage
   - Provide usage examples
   - Include accessibility notes
   - Maintain style guide 