# Slideshow

The Slideshow plugin is a JQuery UI plugin that implements roughly the same functionality of the 
former slideshow object in the RC, while using the new Web Audio API.

Invocation:

```javascript
$(parent).Slideshow(<options>)
```

An example is contained in the file `slideshow.html`.

## Options
 
### Slides
 
The `slides` property is required and points to an array of `slide` structures.
 
#### Slide

A `slide` structure has the following properties

- `image` (required), the URL to the image
- `sound` (optional), pointing either to the string `none`, or to a `sound` structure.
- `duration` (optional), overrides default duration (in seconds) in autoplay mode

If the `sound` property is absent, it means that any previous sound keeps playing. Using `none`
makes the previous sound stop or fade out.

The `sound`  structure has the properties described in the [AudioRegion] document.

A `fade` structure has the following properties:

- `duration` (required), length of fade in seconds
- `type`, being either `linear` (default) or `exponential`

### Style

The `style` object defines the positioning and appearance.

#### Position

The `position` object gives the coordinates on the workspace:

- `left`, `top`, `width`, `height` in pixels

#### Padding

The `padding` corresponds to the same CSS property

- `left`, `top`, `right`, `bottom` in pixels

#### Border

The `border` corresponds to the same CSS property

- `style`, such as `solid`, `dotted` etc.
- `strength`, the border size
- `color`
- `radius`, for rounded edges

#### Background

The `background` corresponds to the same CSS property

- `color`
- `image`, a URL
- `repeat`: `repeat` / `no-repeat`
- `position`: `left-top` etc.
- `size`: `auto`, `cover`, `contain`

#### Opacity

- `opacity` value

### Options

The `options` section is used to customize the slideshow

#### Placement

The `placement` structure has the following properties

- `size`, one of `contain`, `cover`, `fit`, `auto`
- `position`, such as `center-center`

#### Settings

The `settings` structure has the following properties

- `loop`, a boolean
- `navigation`, a boolean (navigation bar)

#### Automate

The `automate` structure has the following properties

- `autoplay`, either of `yes`, `no`, `click`
- `duration`, default value in seconds

#### Audio

The `audio` structure provides defaults for sounds

- `crossfade`, value in seconds
- `fadein`, default fadein (`fade` structure)
- `fadeout`, default fadeout (`fade` structure)
