module HelloElm exposing (..)
import Html exposing (Html, Attribute, div, input, text, h1)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput)
import String


main =
    Html.beginnerProgram { model = model, view = view, update = update }



-- MODEL


type alias Model =
    { name : String
    }


model : Model
model =
    { name = "" }



-- UPDATE


type Msg
    = Change String


update : Msg -> Model -> Model
update msg model =
    case msg of
        Change newContent ->
            { model | name = newContent }



-- VIEW


view : Model -> Html Msg
view model =
    let
        displayName = if String.isEmpty model.name then
                   "Unknown"
               else
                    model.name
    in
        div []
            [ input [ placeholder "Whats your name?", onInput Change ] []
            , h1 [] [ text ("Hello, " ++ displayName ++ "!") ]
            ]